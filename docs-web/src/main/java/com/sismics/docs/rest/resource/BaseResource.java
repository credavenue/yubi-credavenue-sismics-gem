package com.sismics.docs.rest.resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sismics.docs.core.constant.Constants;
import com.sismics.docs.core.dao.GroupDao;
import com.sismics.docs.core.dao.RoleBaseFunctionDao;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.criteria.GroupCriteria;
import com.sismics.docs.core.dao.dto.GroupDto;
import com.sismics.docs.core.model.Auth.JWTModel;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.security.IPrincipal;
import com.sismics.security.UserPrincipal;
import com.sismics.util.JWTUtil;
import com.sismics.util.filter.SecurityFilter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.*;

/**
 * Base class of REST resources.
 *
 * @author jtremeaux
 */
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public abstract class BaseResource {
    /**
     * @apiDefine admin Admin
     * Only the admin user can access this resource
     */

    /**
     * @apiDefine user Authenticated user
     * All authenticated users can access this resource
     */

    /**
     * @apiDefine none Anonymous user
     * This resource can be accessed anonymously
     */

    /**
     * @apiDefine server Server error
     */

    /**
     * @apiDefine client Client error
     */

    /**
     * Injects the HTTP request.
     */
    @Context
    protected HttpServletRequest request;

    /**
     * Application key.
     */
    @QueryParam("app_key")
    protected String appKey;

    /**
     * Principal of the authenticated user.
     */
    protected IPrincipal principal;

    /**
     * This method is used to check if the user is authenticated.
     * Giving priority to Bearer token and then cookies
     *
     * @return True if the user is authenticated and not anonymous
     */
    protected boolean authenticate() {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            // extract token from "Bearer <TOKEN>"
            String token = authHeader.substring(7);
            return trySetTokenPrincipal(token);
        }

        Principal principal = (Principal) request.getAttribute(SecurityFilter.PRINCIPAL_ATTRIBUTE);
        if (principal instanceof IPrincipal) {
            this.principal = (IPrincipal) principal;
            return !this.principal.isAnonymous();
        } else {
            return false;
        }
    }

    /**
     * Checks if the user has a base function. Throw an exception if the check fails.
     *
     * @param baseFunction Base function to check
     */
    void checkBaseFunction(BaseFunction baseFunction) {
        if (!hasBaseFunction(baseFunction)) {
            throw new ForbiddenClientException();
        }
    }

    /**
     * Checks if the user has a base function.
     *
     * @param baseFunction Base function to check
     * @return True if the user has the base function
     */
    boolean hasBaseFunction(BaseFunction baseFunction) {
        if (!(principal instanceof UserPrincipal)) {
            return false;
        }
        Set<String> baseFunctionSet = ((UserPrincipal) principal).getBaseFunctionSet();
        return baseFunctionSet != null && baseFunctionSet.contains(baseFunction.name());
    }

    /**
     * Returns a list of ACL target ID.
     *
     * @param shareId Share ID (optional)
     * @return List of ACL target ID
     */
    List<String> getTargetIdList(String shareId) {
        List<String> targetIdList = Lists.newArrayList(principal.getGroupIdSet());
        if (principal.getId() != null) {
            targetIdList.add(principal.getId());
        }
        if (shareId != null) {
            targetIdList.add(shareId);
        }
        return targetIdList;
    }

    protected JWTModel generateBearerToken(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("name", user.getUsername());
        String secretKey = System.getenv(Constants.JWT_SECRET_KEY);
        long ttl = Long.parseLong(System.getenv(Constants.JWT_TTL_IN_SECONDS));
        return JWTUtil.generateToken(userInfo, secretKey, ttl);
    }

    private boolean trySetTokenPrincipal(String token) {
        // todo: improve this, later
        String secretKey = System.getenv(Constants.JWT_SECRET_KEY);
        try {
            Map<String, Object> userInfo = JWTUtil.verifyToken(token, secretKey);
            String id = (String) userInfo.get("id");
            String name = (String) userInfo.get("name");
            UserDao userDao = new UserDao();
            User user = userDao.getById(id);
            if (user == null) {
                return false;
            }
            UserPrincipal userPrincipal = new UserPrincipal(id, name);

            // Add groups
            GroupDao groupDao = new GroupDao();
            Set<String> groupRoleIdSet = new HashSet<>();
            List<GroupDto> groupDtoList = groupDao.findByCriteria(new GroupCriteria()
                    .setUserId(user.getId())
                    .setRecursive(true), null);
            Set<String> groupIdSet = Sets.newHashSet();
            for (GroupDto groupDto : groupDtoList) {
                groupIdSet.add(groupDto.getId());
                if (groupDto.getRoleId() != null) {
                    groupRoleIdSet.add(groupDto.getRoleId());
                }
            }
            userPrincipal.setGroupIdSet(groupIdSet);

            // Add base functions
            groupRoleIdSet.add(user.getRoleId());
            RoleBaseFunctionDao userBaseFunction = new RoleBaseFunctionDao();
            Set<String> baseFunctionSet = userBaseFunction.findByRoleId(groupRoleIdSet);
            userPrincipal.setBaseFunctionSet(baseFunctionSet);

            // Add email
            userPrincipal.setEmail(user.getEmail());

            this.principal = userPrincipal;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

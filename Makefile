DOCKER_COMPOSE_DEV = docker-compose -f docker-compose-dev.yml

setup_dev:
	# install supporting JDK-11
	brew install openjdk@11
	# syslink the JDK for system to find
	sudo ln -sfn /opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
	brew install maven

project_clean:
	mvn clean

project_build:
	make project_clean
	# skipping test for now
	# todo: include test cases
	mvn -DskipTests install

image_build_dev:
	$(DOCKER_COMPOSE_DEV) build

deploy_dev:
	make project_clean
	make project_build
	make image_build_dev

	$(DOCKER_COMPOSE_DEV) up -d
	$(DOCKER_COMPOSE_DEV) ps

deploy_db_dev:
	$(DOCKER_COMPOSE_DEV) build db
	$(DOCKER_COMPOSE_DEV) up -d db
	$(DOCKER_COMPOSE_DEV) ps

stop_containers_dev:
	$(DOCKER_COMPOSE_DEV) stop
	$(DOCKER_COMPOSE_DEV) ps

log_teedy_service_dev:
	docker logs teedy_service

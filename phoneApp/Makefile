NAME = phone
EXE = $(NAME)
DIR = /usr/local/bin
LOGO_DIR = /usr/share/applications
JAR_DIR = /usr/lib

version = 0.1
description = Desktop Application
JAR_NAME = PjProject-1.0-SNAPSHOT-jar-with-dependencies.jar


all: deb

build:
	mkdir build/
	mkdir -p build/$(DIR)
	mkdir -p build/$(LOGO_DIR)
	mkdir -p build/$(JAR_DIR)
	cp -a phone-app.sh build/$(DIR)/$(EXE)
	cp -a phone.jpg build/$(DIR)/
	cp -a ../target/$(JAR_NAME) build/$(JAR_DIR)/$(NAME).jar
	cp -a phone-app.desktop build/$(LOGO_DIR)/$(NAME).desktop
	chmod +x build/$(DIR)/$(EXE)

deb: build

	fpm -s dir -t deb -n $(EXE)				\
		-v $(version)				\
		--maintainer "guna.com"	\
		-C "build" -a noarch			\
		-d openjdk-11-jdk		\
		-d openjfx			\
		--url "https://guna.com"			\
		--description "$(description)"			\
		.

.PHONY: clean distclean


clean:
	rm -rf build/ deb/
	rm -f *.deb *.spec
	rm -rf dist/


distclean: clean
	rm -f *~


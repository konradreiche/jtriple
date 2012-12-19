#!/bin/sh
mvn -DaltDeploymentRepository=snapshot-repo::default::file:repository/$1s clean deploy

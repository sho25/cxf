begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|osgi
operator|.
name|itests
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|MavenUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|container
operator|.
name|internal
operator|.
name|JavaVersionUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|options
operator|.
name|MavenUrlReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|options
operator|.
name|extra
operator|.
name|VMOption
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|composite
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|mavenBundle
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|systemProperty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|when
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|editConfigurationFilePut
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|karafDistributionConfiguration
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|CXFOSGiTestSupport
block|{
annotation|@
name|Inject
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|protected
name|MavenUrlReference
name|cxfUrl
decl_stmt|;
specifier|protected
name|MavenUrlReference
name|amqUrl
decl_stmt|;
specifier|protected
name|MavenUrlReference
name|springLegacyUrl
decl_stmt|;
specifier|private
specifier|static
name|String
name|getKarafVersion
parameter_list|()
block|{
return|return
name|MavenUtils
operator|.
name|getArtifactVersion
argument_list|(
literal|"org.apache.karaf"
argument_list|,
literal|"apache-karaf"
argument_list|)
return|;
block|}
comment|/**      * Create an {@link org.ops4j.pax.exam.Option} for using a .      *      * @return      */
specifier|protected
name|Option
name|cxfBaseConfig
parameter_list|()
block|{
specifier|final
name|String
name|karafVersion
init|=
name|getKarafVersion
argument_list|()
decl_stmt|;
specifier|final
name|MavenUrlReference
name|karafUrl
init|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"apache-karaf"
argument_list|)
operator|.
name|version
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|type
argument_list|(
literal|"tar.gz"
argument_list|)
decl_stmt|;
name|cxfUrl
operator|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.cxf.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"apache-cxf"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|classifier
argument_list|(
literal|"features"
argument_list|)
expr_stmt|;
name|amqUrl
operator|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.activemq"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"activemq-karaf"
argument_list|)
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|classifier
argument_list|(
literal|"features"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
expr_stmt|;
name|springLegacyUrl
operator|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf.features"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"spring-legacy"
argument_list|)
operator|.
name|version
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|classifier
argument_list|(
literal|"features"
argument_list|)
expr_stmt|;
name|String
name|localRepo
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"localRepository"
argument_list|)
decl_stmt|;
name|Object
name|urp
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"cxf.useRandomFirstPort"
argument_list|)
decl_stmt|;
if|if
condition|(
name|JavaVersionUtil
operator|.
name|getMajorVersion
argument_list|()
operator|>=
literal|9
condition|)
block|{
return|return
name|composite
argument_list|(
name|karafDistributionConfiguration
argument_list|()
operator|.
name|frameworkUrl
argument_list|(
name|karafUrl
argument_list|)
operator|.
name|karafVersion
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|name
argument_list|(
literal|"Apache Karaf"
argument_list|)
operator|.
name|useDeployFolder
argument_list|(
literal|false
argument_list|)
operator|.
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/paxexam/"
argument_list|)
argument_list|)
argument_list|,
comment|//DO NOT COMMIT WITH THIS LINE ENABLED!!!
comment|//KarafDistributionOption.keepRuntimeFolder(),
comment|//debugConfiguration(), // nor this
name|systemProperty
argument_list|(
literal|"pax.exam.osgi.unresolved.fail"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"java.awt.headless"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
argument_list|,
name|when
argument_list|(
name|localRepo
operator|!=
literal|null
argument_list|)
operator|.
name|useOptions
argument_list|(
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|,
literal|"org.ops4j.pax.url.mvn.localRepository"
argument_list|,
name|localRepo
argument_list|)
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-reads=java.xml=java.logging"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/"
operator|+
literal|"org.apache.karaf.specs.locator=java.xml,ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--patch-module"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.base=lib/endorsed/org.apache.karaf.specs.locator-"
operator|+
name|karafVersion
operator|+
literal|".jar"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--patch-module"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.xml=lib/endorsed/org.apache.karaf.specs.java.xml-"
operator|+
name|karafVersion
operator|+
literal|".jar"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.security=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.net=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.lang=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.util=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.naming/javax.naming.spi=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"java.rmi/sun.rmi.transport.tcp=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/sun.net.www.protocol.http=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/sun.net.www.protocol.https=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/sun.net.www.protocol.jar=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=jdk.naming.rmi/com.sun.jndi.url.rmi=ALL-UNNAMED"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"-classpath"
argument_list|)
argument_list|,
operator|new
name|VMOption
argument_list|(
literal|"lib/jdk9plus/*"
operator|+
name|File
operator|.
name|pathSeparator
operator|+
literal|"lib/boot/*"
argument_list|)
argument_list|,
name|when
argument_list|(
name|urp
operator|!=
literal|null
argument_list|)
operator|.
name|useOptions
argument_list|(
name|systemProperty
argument_list|(
literal|"cxf.useRandomFirstPort"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|composite
argument_list|(
name|karafDistributionConfiguration
argument_list|()
operator|.
name|frameworkUrl
argument_list|(
name|karafUrl
argument_list|)
operator|.
name|karafVersion
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|name
argument_list|(
literal|"Apache Karaf"
argument_list|)
operator|.
name|useDeployFolder
argument_list|(
literal|false
argument_list|)
operator|.
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/paxexam/"
argument_list|)
argument_list|)
argument_list|,
comment|//DO NOT COMMIT WITH THIS LINE ENABLED!!!
comment|//KarafDistributionOption.keepRuntimeFolder(),
comment|//debugConfiguration(), // nor this
name|systemProperty
argument_list|(
literal|"pax.exam.osgi.unresolved.fail"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"java.awt.headless"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
argument_list|,
name|when
argument_list|(
name|localRepo
operator|!=
literal|null
argument_list|)
operator|.
name|useOptions
argument_list|(
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|,
literal|"org.ops4j.pax.url.mvn.localRepository"
argument_list|,
name|localRepo
argument_list|)
argument_list|)
argument_list|,
name|when
argument_list|(
name|urp
operator|!=
literal|null
argument_list|)
operator|.
name|useOptions
argument_list|(
name|systemProperty
argument_list|(
literal|"cxf.useRandomFirstPort"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
specifier|protected
specifier|static
name|Option
name|testUtils
parameter_list|()
block|{
return|return
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.cxf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"cxf-testutils"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
return|;
block|}
specifier|protected
name|void
name|assertBundleStarted
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|findBundleByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Bundle "
operator|+
name|name
operator|+
literal|" should be installed"
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bundle "
operator|+
name|name
operator|+
literal|" should be started"
argument_list|,
name|Bundle
operator|.
name|ACTIVE
argument_list|,
name|bundle
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Bundle
name|findBundleByName
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|symbolicName
argument_list|)
condition|)
block|{
return|return
name|bundle
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|assertBlueprintNamespacePublished
parameter_list|(
name|String
name|namespace
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|InvalidSyntaxException
throws|,
name|InterruptedException
block|{
name|AbstractServerActivator
operator|.
name|awaitService
argument_list|(
name|bundleContext
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"(&(objectClass=org.apache.aries.blueprint.NamespaceHandler)"
operator|+
literal|"(osgi.service.blueprint.namespace=%s))"
argument_list|,
name|namespace
argument_list|)
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


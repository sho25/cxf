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
name|xkms
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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|apache
operator|.
name|cxf
operator|.
name|testutil
operator|.
name|common
operator|.
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|extensions
operator|.
name|ResultDetails
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|LocateResultType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|MessageExtensionAbstractType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMajorEnum
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3
operator|.
name|_2002
operator|.
name|_03
operator|.
name|xkms_wsdl
operator|.
name|XKMSPortType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|Configuration
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
name|options
operator|.
name|MavenArtifactUrlReference
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
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
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
name|spi
operator|.
name|reactors
operator|.
name|PerClass
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
name|configureConsole
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
name|features
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
name|replaceConfigurationFile
import|;
end_import

begin_class
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerClass
operator|.
name|class
argument_list|)
specifier|public
class|class
name|BasicIntegrationTest
block|{
comment|// Adding apache snapshots as cxf trunk may contain snapshot dependencies
comment|//private static final String REPOS = "http://repo1.maven.org/maven2@id=central, "
comment|//    + "http://repository.apache.org/content/groups/snapshots-group@snapshots@noreleases@id=apache-snapshots ";
annotation|@
name|Inject
specifier|protected
name|XKMSPortType
name|xkmsService
decl_stmt|;
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|getConfig
parameter_list|()
block|{
name|String
name|port
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|BasicIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"BasicIntegrationTest.PORT"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|String
name|xkmsEndpoint
init|=
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/cxf/XKMS"
decl_stmt|;
name|String
name|karafVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|,
literal|"4.0.8"
argument_list|)
decl_stmt|;
name|String
name|localRepository
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"localRepository"
argument_list|)
decl_stmt|;
name|MavenArtifactUrlReference
name|karafUrl
init|=
name|maven
argument_list|()
comment|//
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf"
argument_list|)
comment|//
operator|.
name|artifactId
argument_list|(
literal|"apache-karaf"
argument_list|)
comment|//
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
name|MavenArtifactUrlReference
name|xkmsFeatures
init|=
name|maven
argument_list|()
comment|//
operator|.
name|groupId
argument_list|(
literal|"org.apache.cxf.services.xkms"
argument_list|)
comment|//
operator|.
name|artifactId
argument_list|(
literal|"cxf-services-xkms-features"
argument_list|)
comment|//
operator|.
name|versionAsInProject
argument_list|()
comment|//
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
decl_stmt|;
return|return
operator|new
name|Option
index|[]
block|{
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
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/paxexam/unpack/"
argument_list|)
argument_list|)
operator|.
name|useDeployFolder
argument_list|(
literal|false
argument_list|)
block|,
name|systemProperty
argument_list|(
literal|"java.awt.headless"
argument_list|)
operator|.
name|value
argument_list|(
literal|"true"
argument_list|)
block|,
name|systemProperty
argument_list|(
literal|"BasicIntegrationTest.PORT"
argument_list|)
operator|.
name|value
argument_list|(
name|port
argument_list|)
block|,
name|copy
argument_list|(
literal|"data/xkms/certificates/trusted_cas/root.cer"
argument_list|)
block|,
name|copy
argument_list|(
literal|"data/xkms/certificates/trusted_cas/wss40CA.cer"
argument_list|)
block|,
name|copy
argument_list|(
literal|"data/xkms/certificates/cas/alice.cer"
argument_list|)
block|,
name|copy
argument_list|(
literal|"data/xkms/certificates/dave.cer"
argument_list|)
block|,
name|copy
argument_list|(
literal|"data/xkms/certificates/http___localhost_8080_services_TestService.cer"
argument_list|)
block|,
name|copy
argument_list|(
literal|"etc/org.ops4j.pax.logging.cfg"
argument_list|)
block|,
comment|//editConfigurationFilePut("etc/org.ops4j.pax.url.mvn.cfg", "org.ops4j.pax.url.mvn.repositories", REPOS),
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.web.cfg"
argument_list|,
literal|"org.osgi.service.http.port"
argument_list|,
name|port
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.cxf.xkms.client.cfg"
argument_list|,
literal|"xkms.endpoint"
argument_list|,
name|xkmsEndpoint
argument_list|)
block|,
name|when
argument_list|(
name|localRepository
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
name|localRepository
argument_list|)
argument_list|)
block|,
name|features
argument_list|(
name|xkmsFeatures
argument_list|,
literal|"cxf-xkms-service"
argument_list|,
literal|"cxf-xkms-client"
argument_list|,
literal|"cxf-xkms-ldap"
argument_list|)
block|,
name|configureConsole
argument_list|()
operator|.
name|ignoreLocalConsole
argument_list|()
block|,
comment|//org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder(),
comment|//CoreOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005")
block|}
return|;
block|}
specifier|protected
name|Option
name|copy
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|replaceConfigurationFile
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/"
operator|+
name|path
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|assertSuccess
parameter_list|(
name|LocateResultType
name|result
parameter_list|)
block|{
name|Iterator
argument_list|<
name|MessageExtensionAbstractType
argument_list|>
name|it
init|=
name|result
operator|.
name|getMessageExtension
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|error
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ResultDetails
name|details
init|=
operator|(
name|ResultDetails
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|error
operator|=
name|details
operator|.
name|getDetails
argument_list|()
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Expecting success but got error "
operator|+
name|error
argument_list|,
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_SUCCESS
operator|.
name|value
argument_list|()
argument_list|,
name|result
operator|.
name|getResultMajor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


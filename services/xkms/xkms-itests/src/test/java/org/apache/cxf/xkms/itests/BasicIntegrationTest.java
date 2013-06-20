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
name|karaf
operator|.
name|options
operator|.
name|LogLevelOption
operator|.
name|LogLevel
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
name|systemTimeout
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
name|keepRuntimeFolder
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
name|logLevel
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
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_PORT
init|=
literal|"9191"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XKMS_ENDPOINT
init|=
literal|"http://localhost:"
operator|+
name|HTTP_PORT
operator|+
literal|"/cxf/XKMS"
decl_stmt|;
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
name|projectVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"project.version"
argument_list|)
decl_stmt|;
name|String
name|karafVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|)
decl_stmt|;
name|MavenArtifactUrlReference
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
literal|"zip"
argument_list|)
decl_stmt|;
name|MavenUrlReference
name|cxfFeatures
init|=
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
name|version
argument_list|(
name|projectVersion
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
decl_stmt|;
name|MavenUrlReference
name|xkmsFeatures
init|=
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.cxf.services.xkms"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"cxf-services-xkms-features"
argument_list|)
operator|.
name|version
argument_list|(
name|projectVersion
argument_list|)
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
comment|/*              * Timeout is set to 15 minutes because installation of cxf and xkms takes ages. The reason should              * be investigated in the near future. One problem is the usage of pax exam snapshot build which              * makes maven scan the snapshot repositories for each dependency but that should not be the main              * reason.              */
name|systemTimeout
argument_list|(
literal|900000
argument_list|)
block|,
name|logLevel
argument_list|(
name|LogLevel
operator|.
name|ERROR
argument_list|)
block|,
name|keepRuntimeFolder
argument_list|()
block|,
name|replaceConfigurationFile
argument_list|(
literal|"data/xkms/certificates/trusted_cas/root.cer"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/xkms/certificates/trusted_cas/root.cer"
argument_list|)
argument_list|)
block|,
name|replaceConfigurationFile
argument_list|(
literal|"data/xkms/certificates/cas/alice.cer"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/xkms/certificates/cas/alice.cer"
argument_list|)
argument_list|)
block|,
name|replaceConfigurationFile
argument_list|(
literal|"etc/org.apache.cxf.xkms.cfg"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/etc/org.apache.cxf.xkms.cfg"
argument_list|)
argument_list|)
block|,
name|features
argument_list|(
name|cxfFeatures
argument_list|,
literal|"cxf"
argument_list|)
block|,
name|features
argument_list|(
name|xkmsFeatures
argument_list|,
literal|"cxf-xkms-service"
argument_list|,
literal|"cxf-xkms-client"
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.web.cfg"
argument_list|,
literal|"org.osgi.service.http.port"
argument_list|,
name|HTTP_PORT
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.cxf.xkms.client.cfg"
argument_list|,
literal|"xkms.endpoint"
argument_list|,
name|XKMS_ENDPOINT
argument_list|)
block|}
return|;
block|}
block|}
end_class

end_unit


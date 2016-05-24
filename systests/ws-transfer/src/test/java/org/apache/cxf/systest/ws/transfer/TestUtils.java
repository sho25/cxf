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
name|systest
operator|.
name|ws
operator|.
name|transfer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|endpoint
operator|.
name|Server
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|systest
operator|.
name|ws
operator|.
name|transfer
operator|.
name|resolver
operator|.
name|MyResourceResolver
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
name|systest
operator|.
name|ws
operator|.
name|transfer
operator|.
name|validator
operator|.
name|StudentPutResourceValidator
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
name|systest
operator|.
name|ws
operator|.
name|transfer
operator|.
name|validator
operator|.
name|TeacherResourceValidator
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
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
name|ws
operator|.
name|transfer
operator|.
name|manager
operator|.
name|MemoryResourceManager
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
name|ws
operator|.
name|transfer
operator|.
name|manager
operator|.
name|ResourceManager
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
name|ws
operator|.
name|transfer
operator|.
name|resource
operator|.
name|Resource
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
name|ws
operator|.
name|transfer
operator|.
name|resource
operator|.
name|ResourceLocal
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
name|ws
operator|.
name|transfer
operator|.
name|resource
operator|.
name|ResourceRemote
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
name|ws
operator|.
name|transfer
operator|.
name|resourcefactory
operator|.
name|ResourceFactory
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
name|ws
operator|.
name|transfer
operator|.
name|resourcefactory
operator|.
name|ResourceFactoryImpl
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
name|ws
operator|.
name|transfer
operator|.
name|shared
operator|.
name|TransferConstants
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
name|ws
operator|.
name|transfer
operator|.
name|validationtransformation
operator|.
name|XSDResourceTypeIdentifier
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
name|ws
operator|.
name|transfer
operator|.
name|validationtransformation
operator|.
name|XSLTResourceTransformer
import|;
end_import

begin_comment
comment|/**  * Parent test for all tests in WS-Transfer System Tests.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TestUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_STUDENTS_URL
init|=
literal|"http://localhost:8080/ResourceStudents"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_FACTORY_URL
init|=
literal|"http://localhost:8080/ResourceFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_TEACHERS_URL
init|=
literal|"http://localhost:8081/ResourceTeachers"
decl_stmt|;
specifier|private
specifier|static
name|Server
name|resourceFactoryServer
decl_stmt|;
specifier|private
specifier|static
name|Server
name|studentsResourceServer
decl_stmt|;
specifier|private
specifier|static
name|Server
name|teachersResourceFactoryServer
decl_stmt|;
specifier|private
specifier|static
name|Server
name|teachersResourceServer
decl_stmt|;
specifier|private
name|TestUtils
parameter_list|()
block|{              }
specifier|protected
specifier|static
name|ResourceFactory
name|createResourceFactoryClient
parameter_list|()
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|transfer
operator|.
name|resourcefactory
operator|.
name|ResourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|RESOURCE_FACTORY_URL
argument_list|)
expr_stmt|;
return|return
operator|(
name|ResourceFactory
operator|)
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
specifier|protected
specifier|static
name|Resource
name|createResourceClient
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Resource
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Resource
name|proxy
init|=
operator|(
name|Resource
operator|)
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// Add reference parameters
name|AddressingProperties
name|addrProps
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|addrProps
operator|.
name|setTo
argument_list|(
name|ref
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|proxy
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES
argument_list|,
name|addrProps
argument_list|)
expr_stmt|;
return|return
name|proxy
return|;
block|}
specifier|protected
specifier|static
name|void
name|createStudentsServers
parameter_list|()
block|{
name|UIDManager
operator|.
name|reset
argument_list|()
expr_stmt|;
name|ResourceManager
name|studentsResourceManager
init|=
operator|new
name|MemoryResourceManager
argument_list|()
decl_stmt|;
name|resourceFactoryServer
operator|=
name|createResourceFactory
argument_list|(
name|studentsResourceManager
argument_list|)
expr_stmt|;
name|studentsResourceServer
operator|=
name|createStudentsResource
argument_list|(
name|studentsResourceManager
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|createTeachersServers
parameter_list|()
block|{
name|ResourceManager
name|teachersResourceManager
init|=
operator|new
name|MemoryResourceManager
argument_list|()
decl_stmt|;
name|ResourceRemote
name|resource
init|=
operator|new
name|ResourceRemote
argument_list|()
decl_stmt|;
name|resource
operator|.
name|setManager
argument_list|(
name|teachersResourceManager
argument_list|)
expr_stmt|;
name|resource
operator|.
name|getResourceTypeIdentifiers
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XSDResourceTypeIdentifier
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/schema/teacher.xsd"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|XSLTResourceTransformer
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/xslt/teacherDefaultValues.xsl"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|TeacherResourceValidator
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|teachersResourceFactoryServer
operator|=
name|createTeachersResourceFactoryEndpoint
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|teachersResourceServer
operator|=
name|createTeacherResourceEndpoint
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|destroyStudentsServers
parameter_list|()
block|{
name|resourceFactoryServer
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|studentsResourceServer
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|destroyTeachersServers
parameter_list|()
block|{
name|teachersResourceFactoryServer
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|teachersResourceServer
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|Server
name|createResourceFactory
parameter_list|(
name|ResourceManager
name|resourceManager
parameter_list|)
block|{
name|ResourceFactoryImpl
name|resourceFactory
init|=
operator|new
name|ResourceFactoryImpl
argument_list|()
decl_stmt|;
name|resourceFactory
operator|.
name|setResourceResolver
argument_list|(
operator|new
name|MyResourceResolver
argument_list|(
name|RESOURCE_STUDENTS_URL
argument_list|,
name|resourceManager
argument_list|,
name|RESOURCE_TEACHERS_URL
argument_list|)
argument_list|)
expr_stmt|;
name|resourceFactory
operator|.
name|getResourceTypeIdentifiers
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XSDResourceTypeIdentifier
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/schema/studentCreate.xsd"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|XSLTResourceTransformer
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/xslt/studentCreate.xsl"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|resourceFactory
operator|.
name|getResourceTypeIdentifiers
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XSDResourceTypeIdentifier
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/schema/teacherCreateBasic.xsd"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|XSLTResourceTransformer
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/xslt/teacherCreateBasic.xsl"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|transfer
operator|.
name|resourcefactory
operator|.
name|ResourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
name|resourceFactory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|RESOURCE_FACTORY_URL
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|Server
name|createStudentsResource
parameter_list|(
name|ResourceManager
name|resourceManager
parameter_list|)
block|{
name|ResourceLocal
name|resourceLocal
init|=
operator|new
name|ResourceLocal
argument_list|()
decl_stmt|;
name|resourceLocal
operator|.
name|setManager
argument_list|(
name|resourceManager
argument_list|)
expr_stmt|;
name|resourceLocal
operator|.
name|getResourceTypeIdentifiers
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XSDResourceTypeIdentifier
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/schema/studentPut.xsd"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|XSLTResourceTransformer
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/xslt/studentPut.xsl"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|StudentPutResourceValidator
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Resource
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
name|resourceLocal
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|RESOURCE_STUDENTS_URL
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|Server
name|createTeachersResourceFactoryEndpoint
parameter_list|(
name|ResourceRemote
name|resource
parameter_list|)
block|{
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|ResourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|RESOURCE_TEACHERS_URL
operator|+
name|TransferConstants
operator|.
name|RESOURCE_REMOTE_SUFFIX
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|Server
name|createTeacherResourceEndpoint
parameter_list|(
name|ResourceRemote
name|resource
parameter_list|)
block|{
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Resource
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|RESOURCE_TEACHERS_URL
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit


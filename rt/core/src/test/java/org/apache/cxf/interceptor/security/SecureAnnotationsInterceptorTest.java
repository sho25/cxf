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
name|interceptor
operator|.
name|security
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|frontend
operator|.
name|MethodDispatcher
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|security
operator|.
name|SecurityContext
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|SecureAnnotationsInterceptorTest
extends|extends
name|Assert
block|{
specifier|private
name|Method
name|method
decl_stmt|;
specifier|private
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|method
operator|=
name|TestService
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"echo"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
operator|new
name|TestSecurityContext
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|MethodDispatcher
name|md
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MethodDispatcher
operator|.
name|class
argument_list|)
decl_stmt|;
name|service
operator|.
name|get
argument_list|(
name|MethodDispatcher
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|md
operator|.
name|getMethod
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|service
argument_list|,
name|md
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPermitWithNoRoles
parameter_list|()
block|{
operator|new
name|SecureAnnotationsInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPermitWithMethodRoles
parameter_list|()
block|{
name|SecureAnnotationsInterceptor
name|in
init|=
operator|new
name|SecureAnnotationsInterceptor
argument_list|()
decl_stmt|;
name|in
operator|.
name|setAnnotationClassName
argument_list|(
name|SecureRolesAllowed
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setSecuredObject
argument_list|(
operator|new
name|TestService
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AccessDeniedException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAccessDeniedMethodRoles
parameter_list|()
block|{
name|SecureAnnotationsInterceptor
name|in
init|=
operator|new
name|SecureAnnotationsInterceptor
argument_list|()
decl_stmt|;
name|in
operator|.
name|setAnnotationClassName
argument_list|(
name|SecureRolesAllowed
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setSecuredObject
argument_list|(
operator|new
name|TestService2
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
specifier|public
annotation_defn|@interface
name|SecureRolesAllowed
block|{
name|String
index|[]
name|value
parameter_list|()
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|TestService
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|SecureRolesAllowed
argument_list|(
literal|"testRole"
argument_list|)
specifier|public
name|void
name|echo
parameter_list|()
block|{         }
block|}
specifier|private
specifier|static
class|class
name|TestService2
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|SecureRolesAllowed
argument_list|(
literal|"baz"
argument_list|)
specifier|public
name|void
name|echo
parameter_list|()
block|{         }
block|}
specifier|private
specifier|static
class|class
name|TestSecurityContext
implements|implements
name|SecurityContext
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
literal|"testRole"
operator|.
name|equals
argument_list|(
name|role
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit


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
name|microprofile
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DELETE
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PUT
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|RestClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|RestClientDefinitionException
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
name|Test
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
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|ValidatorTest
block|{
specifier|public
specifier|abstract
specifier|static
class|class
name|NotAnInterface
block|{
annotation|@
name|GET
specifier|public
specifier|abstract
name|Response
name|get
parameter_list|()
function_decl|;
block|}
specifier|public
interface|interface
name|MultiVerbMethod
block|{
annotation|@
name|GET
name|Response
name|get
parameter_list|()
function_decl|;
annotation|@
name|PUT
name|Response
name|put
parameter_list|(
name|String
name|x
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|DELETE
name|Response
name|postAndDelete
parameter_list|()
function_decl|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/rest/{class}"
argument_list|)
specifier|public
interface|interface
name|UnresolvedClassUriTemplate
block|{
annotation|@
name|GET
name|Response
name|myUnresolvedMethod
parameter_list|()
function_decl|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/rest"
argument_list|)
specifier|public
interface|interface
name|UnresolvedMethodUriTemplate
block|{
annotation|@
name|Path
argument_list|(
literal|"/{method}"
argument_list|)
annotation|@
name|GET
name|Response
name|myOtherUnresolvedMethod
parameter_list|()
function_decl|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/rest/{class}"
argument_list|)
specifier|public
interface|interface
name|PartiallyResolvedUriTemplate
block|{
annotation|@
name|GET
name|Response
name|get
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"class"
argument_list|)
name|String
name|className
parameter_list|)
function_decl|;
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/{method}"
argument_list|)
name|Response
name|put
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"method"
argument_list|)
name|String
name|methodName
parameter_list|)
function_decl|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/rest/{class}"
argument_list|)
specifier|public
interface|interface
name|PartiallyResolvedUriTemplate2
block|{
annotation|@
name|DELETE
name|Response
name|delete
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"class"
argument_list|)
name|String
name|className
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/{method}"
argument_list|)
name|Response
name|post
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"class"
argument_list|)
name|String
name|className
parameter_list|)
function_decl|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/rest"
argument_list|)
specifier|public
interface|interface
name|ExtraParamTemplate
block|{
annotation|@
name|GET
name|Response
name|get
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"any"
argument_list|)
name|String
name|any
parameter_list|)
function_decl|;
block|}
specifier|private
specifier|static
name|RestClientBuilder
name|newBuilder
parameter_list|()
block|{
name|RestClientBuilder
name|builder
init|=
name|RestClientBuilder
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|=
name|builder
operator|.
name|baseUrl
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:8080/test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"MalformedURL - bad testcase"
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotAnInterface
parameter_list|()
block|{
name|test
argument_list|(
name|NotAnInterface
operator|.
name|class
argument_list|,
literal|"is not an interface"
argument_list|,
literal|"NotAnInterface"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMethodWithMultipleVerbs
parameter_list|()
block|{
name|test
argument_list|(
name|MultiVerbMethod
operator|.
name|class
argument_list|,
literal|"more than one HTTP method"
argument_list|,
literal|"postAndDelete"
argument_list|,
literal|"javax.ws.rs.POST"
argument_list|,
literal|"javax.ws.rs.DELETE"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnresolvedUriTemplates
parameter_list|()
block|{
name|test
argument_list|(
name|UnresolvedClassUriTemplate
operator|.
name|class
argument_list|,
literal|"unresolved path template variables"
argument_list|,
literal|"UnresolvedClassUriTemplate"
argument_list|,
literal|"myUnresolvedMethod"
argument_list|)
expr_stmt|;
name|test
argument_list|(
name|UnresolvedMethodUriTemplate
operator|.
name|class
argument_list|,
literal|"unresolved path template variables"
argument_list|,
literal|"UnresolvedMethodUriTemplate"
argument_list|,
literal|"myOtherUnresolvedMethod"
argument_list|)
expr_stmt|;
name|test
argument_list|(
name|PartiallyResolvedUriTemplate
operator|.
name|class
argument_list|,
literal|"unresolved path template variables"
argument_list|,
literal|"PartiallyResolvedUriTemplate"
argument_list|,
literal|"put"
argument_list|)
expr_stmt|;
name|test
argument_list|(
name|PartiallyResolvedUriTemplate2
operator|.
name|class
argument_list|,
literal|"unresolved path template variables"
argument_list|,
literal|"PartiallyResolvedUriTemplate2"
argument_list|,
literal|"post"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMissingTemplate
parameter_list|()
block|{
name|test
argument_list|(
name|ExtraParamTemplate
operator|.
name|class
argument_list|,
literal|"extra path segments"
argument_list|,
literal|"ExtraParamTemplate"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|test
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clientInterface
parameter_list|,
name|String
modifier|...
name|expectedMessageTexts
parameter_list|)
block|{
try|try
block|{
name|newBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|clientInterface
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected RestClientDefinitionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RestClientDefinitionException
name|ex
parameter_list|)
block|{
name|String
name|msgText
init|=
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No message text in RestClientDefinitionException"
argument_list|,
name|msgText
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|expectedMessageText
range|:
name|expectedMessageTexts
control|)
block|{
name|assertTrue
argument_list|(
literal|"Exception text does not contain expected message: "
operator|+
name|expectedMessageText
argument_list|,
name|msgText
operator|.
name|contains
argument_list|(
name|expectedMessageText
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit


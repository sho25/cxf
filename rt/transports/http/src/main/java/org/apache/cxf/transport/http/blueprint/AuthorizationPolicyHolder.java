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
name|transport
operator|.
name|http
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
import|;
end_import

begin_class
specifier|public
class|class
name|AuthorizationPolicyHolder
extends|extends
name|AuthorizationPolicy
block|{
specifier|private
name|String
name|parsedElement
decl_stmt|;
specifier|private
name|AuthorizationPolicy
name|authorizationPolicy
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbClasses
decl_stmt|;
specifier|public
name|AuthorizationPolicyHolder
parameter_list|()
block|{     }
specifier|public
name|void
name|init
parameter_list|()
block|{
try|try
block|{
name|DocumentBuilderFactory
name|docFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|docFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
name|docFactory
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|parsedElement
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|authorizationPolicy
operator|=
operator|(
name|AuthorizationPolicy
operator|)
name|HolderUtils
operator|.
name|getJaxbObject
argument_list|(
name|element
argument_list|,
name|AuthorizationPolicy
operator|.
name|class
argument_list|,
name|jaxbContext
argument_list|,
name|jaxbClasses
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAuthorization
argument_list|(
name|authorizationPolicy
operator|.
name|getAuthorization
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAuthorizationType
argument_list|(
name|authorizationPolicy
operator|.
name|getAuthorizationType
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassword
argument_list|(
name|authorizationPolicy
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|setUserName
argument_list|(
name|authorizationPolicy
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not process configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{              }
specifier|public
name|String
name|getParsedElement
parameter_list|()
block|{
return|return
name|parsedElement
return|;
block|}
specifier|public
name|void
name|setParsedElement
parameter_list|(
name|String
name|parsedElement
parameter_list|)
block|{
name|this
operator|.
name|parsedElement
operator|=
name|parsedElement
expr_stmt|;
block|}
block|}
end_class

end_unit


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
name|ws
operator|.
name|security
operator|.
name|wss4j
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|security
operator|.
name|SecurityConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|RequestData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|validate
operator|.
name|Validator
import|;
end_import

begin_class
specifier|public
class|class
name|CXFRequestData
extends|extends
name|RequestData
block|{
specifier|private
specifier|static
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|validatorKeys
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|SAML_TOKEN
argument_list|,
name|SecurityConstants
operator|.
name|SAML1_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|SAML2_TOKEN
argument_list|,
name|SecurityConstants
operator|.
name|SAML2_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|USERNAME_TOKEN
argument_list|,
name|SecurityConstants
operator|.
name|USERNAME_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|SIGNATURE
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|TIMESTAMP
argument_list|,
name|SecurityConstants
operator|.
name|TIMESTAMP_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|BINARY_TOKEN
argument_list|,
name|SecurityConstants
operator|.
name|BST_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|SECURITY_CONTEXT_TOKEN_05_02
argument_list|,
name|SecurityConstants
operator|.
name|SCT_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
name|validatorKeys
operator|.
name|put
argument_list|(
name|WSConstants
operator|.
name|SECURITY_CONTEXT_TOKEN_05_12
argument_list|,
name|SecurityConstants
operator|.
name|SCT_TOKEN_VALIDATOR
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CXFRequestData
parameter_list|()
block|{     }
specifier|public
name|Validator
name|getValidator
parameter_list|(
name|QName
name|qName
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|String
name|key
init|=
name|validatorKeys
operator|.
name|get
argument_list|(
name|qName
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|this
operator|.
name|getMsgContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|o
init|=
operator|(
operator|(
name|SoapMessage
operator|)
name|this
operator|.
name|getMsgContext
argument_list|()
operator|)
operator|.
name|getContextualProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|o
operator|instanceof
name|Validator
condition|)
block|{
return|return
operator|(
name|Validator
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Class
condition|)
block|{
return|return
call|(
name|Validator
call|)
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|o
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|Validator
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|,
name|CXFRequestData
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"Cannot load Validator: "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|t
parameter_list|)
block|{
throw|throw
name|t
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|super
operator|.
name|getValidator
argument_list|(
name|qName
argument_list|)
return|;
block|}
block|}
end_class

end_unit


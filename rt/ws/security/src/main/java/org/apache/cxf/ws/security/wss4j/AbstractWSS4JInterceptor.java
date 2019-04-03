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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapInterceptor
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
name|interceptor
operator|.
name|Fault
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
name|MessageUtils
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
name|phase
operator|.
name|PhaseInterceptor
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
name|rt
operator|.
name|security
operator|.
name|utils
operator|.
name|SecurityUtils
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
name|ConfigurationConstants
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
name|WSS4JConstants
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
name|crypto
operator|.
name|Crypto
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
name|crypto
operator|.
name|PasswordEncryptor
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
name|handler
operator|.
name|WSHandler
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractWSS4JInterceptor
extends|extends
name|WSHandler
implements|implements
name|SoapInterceptor
implements|,
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|QName
argument_list|>
name|HEADERS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|HEADERS
operator|.
name|add
argument_list|(
operator|new
name|QName
argument_list|(
name|WSS4JConstants
operator|.
name|WSSE_NS
argument_list|,
literal|"Security"
argument_list|)
argument_list|)
expr_stmt|;
name|HEADERS
operator|.
name|add
argument_list|(
operator|new
name|QName
argument_list|(
name|WSS4JConstants
operator|.
name|ENC_NS
argument_list|,
literal|"EncryptedData"
argument_list|)
argument_list|)
expr_stmt|;
name|HEADERS
operator|.
name|add
argument_list|(
operator|new
name|QName
argument_list|(
name|WSS4JConstants
operator|.
name|WSSE11_NS
argument_list|,
literal|"EncryptedHeader"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|before
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|after
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|phase
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|public
name|AbstractWSS4JInterceptor
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|id
operator|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|URI
argument_list|>
name|getRoles
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{     }
specifier|public
name|void
name|postHandleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{     }
specifier|public
name|Collection
argument_list|<
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|getAdditionalInterceptors
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getPhase
parameter_list|()
block|{
return|return
name|phase
return|;
block|}
specifier|public
name|void
name|setPhase
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|this
operator|.
name|phase
operator|=
name|phase
expr_stmt|;
block|}
specifier|public
name|Object
name|getOption
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|(
name|Object
name|msgContext
parameter_list|)
block|{
return|return
call|(
name|String
call|)
argument_list|(
operator|(
name|Message
operator|)
name|msgContext
argument_list|)
operator|.
name|getContextualProperty
argument_list|(
literal|"password"
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|Object
name|msgContext
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|msgContext
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|obj
init|=
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|key
argument_list|,
operator|(
name|Message
operator|)
name|msgContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
name|obj
operator|=
name|getOption
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|Object
name|msgContext
parameter_list|,
name|String
name|password
parameter_list|)
block|{
operator|(
operator|(
name|Message
operator|)
name|msgContext
operator|)
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|Object
name|msgContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
operator|(
operator|(
name|Message
operator|)
name|msgContext
operator|)
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getUnderstoodHeaders
parameter_list|()
block|{
return|return
name|HEADERS
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getAfter
parameter_list|()
block|{
return|return
name|after
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getBefore
parameter_list|()
block|{
return|return
name|before
return|;
block|}
specifier|protected
name|boolean
name|isRequestor
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
return|return
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|protected
name|void
name|translateProperties
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
block|{
name|String
name|bspCompliant
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|)
decl_stmt|;
if|if
condition|(
name|bspCompliant
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|,
name|bspCompliant
argument_list|)
expr_stmt|;
block|}
name|String
name|futureTTL
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TIMESTAMP_FUTURE_TTL
argument_list|)
decl_stmt|;
if|if
condition|(
name|futureTTL
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|TTL_FUTURE_TIMESTAMP
argument_list|,
name|futureTTL
argument_list|)
expr_stmt|;
block|}
name|String
name|ttl
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TIMESTAMP_TTL
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttl
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|TTL_TIMESTAMP
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
block|}
name|String
name|utFutureTTL
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|USERNAMETOKEN_FUTURE_TTL
argument_list|)
decl_stmt|;
if|if
condition|(
name|utFutureTTL
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|TTL_FUTURE_USERNAMETOKEN
argument_list|,
name|utFutureTTL
argument_list|)
expr_stmt|;
block|}
name|String
name|utTTL
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|USERNAMETOKEN_TTL
argument_list|)
decl_stmt|;
if|if
condition|(
name|utTTL
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|TTL_USERNAMETOKEN
argument_list|,
name|utTTL
argument_list|)
expr_stmt|;
block|}
name|String
name|certConstraints
init|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|SUBJECT_CERT_CONSTRAINTS
argument_list|,
name|msg
argument_list|)
decl_stmt|;
if|if
condition|(
name|certConstraints
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_SUBJECT_CERT_CONSTRAINTS
argument_list|,
name|certConstraints
argument_list|)
expr_stmt|;
block|}
comment|// Now set SAML SenderVouches + Holder Of Key requirements
name|String
name|valSAMLSubjectConf
init|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|VALIDATE_SAML_SUBJECT_CONFIRMATION
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|boolean
name|validateSAMLSubjectConf
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|valSAMLSubjectConf
operator|!=
literal|null
condition|)
block|{
name|validateSAMLSubjectConf
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|valSAMLSubjectConf
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|VALIDATE_SAML_SUBJECT_CONFIRMATION
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|validateSAMLSubjectConf
argument_list|)
argument_list|)
expr_stmt|;
name|PasswordEncryptor
name|passwordEncryptor
init|=
operator|(
name|PasswordEncryptor
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|PASSWORD_ENCRYPTOR_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|passwordEncryptor
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|PASSWORD_ENCRYPTOR_INSTANCE
argument_list|,
name|passwordEncryptor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Crypto
name|loadCryptoFromPropertiesFile
parameter_list|(
name|String
name|propFilename
parameter_list|,
name|RequestData
name|reqData
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Message
name|message
init|=
operator|(
name|Message
operator|)
name|reqData
operator|.
name|getMsgContext
argument_list|()
decl_stmt|;
name|ClassLoader
name|classLoader
init|=
name|this
operator|.
name|getClassLoader
argument_list|(
name|reqData
operator|.
name|getMsgContext
argument_list|()
argument_list|)
decl_stmt|;
name|PasswordEncryptor
name|passwordEncryptor
init|=
name|getPasswordEncryptor
argument_list|(
name|reqData
argument_list|)
decl_stmt|;
return|return
name|WSS4JUtils
operator|.
name|loadCryptoFromPropertiesFile
argument_list|(
name|message
argument_list|,
name|propFilename
argument_list|,
name|classLoader
argument_list|,
name|passwordEncryptor
argument_list|)
return|;
block|}
block|}
end_class

end_unit


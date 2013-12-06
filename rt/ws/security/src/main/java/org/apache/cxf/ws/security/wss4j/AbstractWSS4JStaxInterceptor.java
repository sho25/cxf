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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Properties
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|logging
operator|.
name|LogUtils
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
name|resource
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
name|CryptoFactory
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
name|JasyptPasswordEncryptor
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
name|WSPasswordCallback
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
name|common
operator|.
name|util
operator|.
name|Loader
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
name|stax
operator|.
name|ConfigurationConverter
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
name|stax
operator|.
name|ext
operator|.
name|WSSConstants
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
name|stax
operator|.
name|ext
operator|.
name|WSSSecurityProperties
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractWSS4JStaxInterceptor
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
argument_list|<
name|QName
argument_list|>
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
name|WSSConstants
operator|.
name|NS_WSSE10
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
name|WSSConstants
operator|.
name|NS_WSSE11
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
name|WSSConstants
operator|.
name|NS_XMLENC
argument_list|,
literal|"EncryptedData"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractWSS4JStaxInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Crypto
argument_list|>
name|cryptos
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Crypto
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|WSSSecurityProperties
name|userSecurityProperties
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|before
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|after
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
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
name|AbstractWSS4JStaxInterceptor
parameter_list|(
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
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
name|userSecurityProperties
operator|=
name|securityProperties
expr_stmt|;
block|}
specifier|public
name|AbstractWSS4JStaxInterceptor
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
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|protected
name|WSSSecurityProperties
name|createSecurityProperties
parameter_list|()
block|{
if|if
condition|(
name|userSecurityProperties
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|WSSSecurityProperties
argument_list|(
name|userSecurityProperties
argument_list|)
return|;
block|}
else|else
block|{
name|WSSSecurityProperties
name|securityProperties
init|=
operator|new
name|WSSSecurityProperties
argument_list|()
decl_stmt|;
name|ConfigurationConverter
operator|.
name|parseActions
argument_list|(
name|properties
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
name|ConfigurationConverter
operator|.
name|parseUserProperties
argument_list|(
name|properties
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
name|ConfigurationConverter
operator|.
name|parseCallback
argument_list|(
name|properties
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
name|ConfigurationConverter
operator|.
name|parseBooleanProperties
argument_list|(
name|properties
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
name|ConfigurationConverter
operator|.
name|parseNonBooleanProperties
argument_list|(
name|properties
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
return|return
name|securityProperties
return|;
block|}
block|}
specifier|protected
name|void
name|translateProperties
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
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
name|securityProperties
operator|.
name|setDisableBSPEnforcement
argument_list|(
operator|!
name|Boolean
operator|.
name|valueOf
argument_list|(
name|bspCompliant
argument_list|)
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
name|securityProperties
operator|.
name|setTimeStampFutureTTL
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|futureTTL
argument_list|)
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
name|securityProperties
operator|.
name|setTimestampTTL
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|ttl
argument_list|)
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
name|securityProperties
operator|.
name|setUtFutureTTL
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|utFutureTTL
argument_list|)
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
name|securityProperties
operator|.
name|setUtTTL
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|utTTL
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|certConstraints
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
name|SUBJECT_CERT_CONSTRAINTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|certConstraints
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setSubjectCertConstraints
argument_list|(
name|convertCertConstraints
argument_list|(
name|certConstraints
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Now set SAML SenderVouches + Holder Of Key requirements
name|String
name|validateSAMLSubjectConf
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
name|VALIDATE_SAML_SUBJECT_CONFIRMATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|validateSAMLSubjectConf
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setValidateSamlSubjectConfirmation
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|validateSAMLSubjectConf
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|actor
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
name|ACTOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|actor
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setActor
argument_list|(
name|actor
argument_list|)
expr_stmt|;
block|}
name|boolean
name|mustUnderstand
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|msg
argument_list|,
name|SecurityConstants
operator|.
name|MUST_UNDERSTAND
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|securityProperties
operator|.
name|setMustUnderstand
argument_list|(
name|mustUnderstand
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|convertCertConstraints
parameter_list|(
name|String
name|certConstraints
parameter_list|)
block|{
name|String
index|[]
name|certConstraintsList
init|=
name|certConstraints
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|certConstraintsList
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|subjectCertConstraints
init|=
operator|new
name|ArrayList
argument_list|<
name|Pattern
argument_list|>
argument_list|(
name|certConstraintsList
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|certConstraint
range|:
name|certConstraintsList
control|)
block|{
try|try
block|{
name|subjectCertConstraints
operator|.
name|add
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|certConstraint
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|subjectCertConstraints
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|configureCallbackHandler
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Object
name|o
init|=
name|soapMessage
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|o
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
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
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// If we have a "password" but no CallbackHandler then construct one
if|if
condition|(
name|o
operator|==
literal|null
operator|&&
name|getPassword
argument_list|(
name|soapMessage
argument_list|)
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|password
init|=
name|getPassword
argument_list|(
name|soapMessage
argument_list|)
decl_stmt|;
name|o
operator|=
operator|new
name|CallbackHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|Callback
name|callback
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|callback
operator|instanceof
name|WSPasswordCallback
condition|)
block|{
name|WSPasswordCallback
name|wsPasswordCallback
init|=
operator|(
name|WSPasswordCallback
operator|)
name|callback
decl_stmt|;
name|wsPasswordCallback
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|CallbackHandler
condition|)
block|{
name|securityProperties
operator|.
name|setCallbackHandler
argument_list|(
operator|(
name|CallbackHandler
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
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
name|Object
name|obj
init|=
operator|(
operator|(
name|Message
operator|)
name|msgContext
operator|)
operator|.
name|getContextualProperty
argument_list|(
name|key
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
name|void
name|setAfter
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|after
parameter_list|)
block|{
name|this
operator|.
name|after
operator|=
name|after
expr_stmt|;
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
specifier|public
name|void
name|setBefore
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|before
parameter_list|)
block|{
name|this
operator|.
name|before
operator|=
name|before
expr_stmt|;
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
comment|/**      * Load a Crypto instance. Firstly, it tries to use the cryptoPropertyRefId tag to retrieve      * a Crypto object via a custom reference Id. Failing this, it tries to load the crypto       * instance via the cryptoPropertyFile tag.      */
specifier|protected
name|Crypto
name|loadCrypto
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|,
name|String
name|cryptoPropertyFile
parameter_list|,
name|String
name|cryptoPropertyRefId
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Crypto
name|crypto
init|=
literal|null
decl_stmt|;
comment|//
comment|// Try the Property Ref Id first
comment|//
name|String
name|refId
init|=
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|soapMessage
argument_list|,
name|cryptoPropertyRefId
argument_list|)
decl_stmt|;
if|if
condition|(
name|refId
operator|!=
literal|null
condition|)
block|{
name|crypto
operator|=
name|cryptos
operator|.
name|get
argument_list|(
name|refId
argument_list|)
expr_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|Object
name|obj
init|=
name|getProperty
argument_list|(
name|soapMessage
argument_list|,
name|refId
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Properties
condition|)
block|{
name|crypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
operator|(
name|Properties
operator|)
name|obj
argument_list|,
name|getClassLoader
argument_list|()
argument_list|,
name|getPasswordEncryptor
argument_list|(
name|soapMessage
argument_list|,
name|securityProperties
argument_list|)
argument_list|)
expr_stmt|;
name|cryptos
operator|.
name|put
argument_list|(
name|refId
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Crypto
condition|)
block|{
name|crypto
operator|=
operator|(
name|Crypto
operator|)
name|obj
expr_stmt|;
name|cryptos
operator|.
name|put
argument_list|(
name|refId
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The Crypto reference "
operator|+
name|refId
operator|+
literal|" specified by "
operator|+
name|cryptoPropertyRefId
operator|+
literal|" could not be loaded"
argument_list|)
expr_stmt|;
block|}
block|}
comment|//
comment|// Now try loading the properties file
comment|//
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|String
name|propFile
init|=
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|soapMessage
argument_list|,
name|cryptoPropertyFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|propFile
operator|!=
literal|null
condition|)
block|{
name|crypto
operator|=
name|cryptos
operator|.
name|get
argument_list|(
name|propFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|loadCryptoFromPropertiesFile
argument_list|(
name|soapMessage
argument_list|,
name|propFile
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
name|cryptos
operator|.
name|put
argument_list|(
name|propFile
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The Crypto properties file "
operator|+
name|propFile
operator|+
literal|" specified by "
operator|+
name|cryptoPropertyFile
operator|+
literal|" could not be loaded or found"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|crypto
return|;
block|}
specifier|protected
name|Crypto
name|loadCryptoFromPropertiesFile
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|,
name|String
name|propFilename
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|ClassLoaderHolder
name|orig
init|=
literal|null
decl_stmt|;
try|try
block|{
try|try
block|{
name|URL
name|url
init|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|propFilename
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|ResourceManager
name|manager
init|=
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|manager
operator|.
name|resolveResource
argument_list|(
literal|""
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|orig
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|url
operator|=
name|manager
operator|.
name|resolveResource
argument_list|(
name|propFilename
argument_list|,
name|URL
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|props
argument_list|,
name|getClassLoader
argument_list|()
argument_list|,
name|getPasswordEncryptor
argument_list|(
name|soapMessage
argument_list|,
name|securityProperties
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|propFilename
argument_list|,
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|orig
operator|!=
literal|null
condition|)
block|{
name|orig
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|PasswordEncryptor
name|getPasswordEncryptor
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
block|{
name|PasswordEncryptor
name|passwordEncryptor
init|=
operator|(
name|PasswordEncryptor
operator|)
name|soapMessage
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
return|return
name|passwordEncryptor
return|;
block|}
name|CallbackHandler
name|callbackHandler
init|=
name|securityProperties
operator|.
name|getCallbackHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackHandler
operator|==
literal|null
condition|)
block|{
name|callbackHandler
operator|=
operator|(
name|CallbackHandler
operator|)
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|ConfigurationConstants
operator|.
name|PW_CALLBACK_REF
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|callbackHandler
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|JasyptPasswordEncryptor
argument_list|(
name|callbackHandler
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
try|try
block|{
return|return
name|Loader
operator|.
name|getTCL
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit


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
name|Collection
import|;
end_import

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Bus
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
name|policy
operator|.
name|AssertionInfo
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
name|policy
operator|.
name|AssertionInfoMap
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
name|policy
operator|.
name|PolicyAssertion
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|SP11Constants
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
name|policy
operator|.
name|SP12Constants
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
name|policy
operator|.
name|SPConstants
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
name|policy
operator|.
name|model
operator|.
name|AsymmetricBinding
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
name|policy
operator|.
name|model
operator|.
name|SupportingToken
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
name|policy
operator|.
name|model
operator|.
name|SymmetricBinding
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
name|policy
operator|.
name|model
operator|.
name|Token
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
name|policy
operator|.
name|model
operator|.
name|UsernameToken
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
name|policy
operator|.
name|model
operator|.
name|Wss11
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|handler
operator|.
name|WSHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|handler
operator|.
name|WSHandlerConstants
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
name|WSConstants
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
name|WSConstants
operator|.
name|WSSE11_NS
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
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
literal|"EncryptedData"
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
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
specifier|private
name|boolean
name|isRequestor
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|containsKey
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|policyAsserted
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|PolicyAssertion
name|assertion
parameter_list|)
block|{
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
decl_stmt|;
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|assertion
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
if|if
condition|(
name|ai
operator|.
name|getAssertion
argument_list|()
operator|==
name|assertion
condition|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|policyAsserted
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|qn
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
decl_stmt|;
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|qn
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|Properties
name|getProps
parameter_list|(
name|Object
name|o
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Properties
condition|)
block|{
name|properties
operator|=
operator|(
name|Properties
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|ResourceManager
name|rm
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
name|rm
operator|.
name|resolveResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|URL
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|AbstractWSS4JInterceptor
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
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|URL
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
operator|(
operator|(
name|URL
operator|)
name|o
operator|)
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|properties
return|;
block|}
name|String
name|addToAction
parameter_list|(
name|String
name|action
parameter_list|,
name|String
name|val
parameter_list|,
name|boolean
name|pre
parameter_list|)
block|{
if|if
condition|(
name|action
operator|.
name|contains
argument_list|(
name|val
argument_list|)
condition|)
block|{
return|return
name|action
return|;
block|}
if|if
condition|(
name|pre
condition|)
block|{
return|return
name|val
operator|+
literal|" "
operator|+
name|action
return|;
block|}
return|return
name|action
operator|+
literal|" "
operator|+
name|val
return|;
block|}
name|boolean
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|q
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
name|String
name|assertAsymetricBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|action
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|AsymmetricBinding
name|abinding
init|=
operator|(
name|AsymmetricBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|abinding
operator|.
name|getProtectionOrder
argument_list|()
operator|==
name|SPConstants
operator|.
name|ProtectionOrder
operator|.
name|EncryptBeforeSigning
condition|)
block|{
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Signature"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Encrypt"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Encrypt"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Signature"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|Object
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
decl_stmt|;
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
literal|"SignaturePropRefId"
argument_list|,
literal|"SigRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"SigRefId"
argument_list|,
name|getProps
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"decryptionPropRefId"
argument_list|,
literal|"DecRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"DecRefId"
argument_list|,
name|getProps
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|put
argument_list|(
literal|"SignaturePropRefId"
argument_list|,
literal|"SigRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"SigRefId"
argument_list|,
name|getProps
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"decryptionPropRefId"
argument_list|,
literal|"DecRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"DecRefId"
argument_list|,
name|getProps
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|abinding
operator|.
name|getInitiatorToken
argument_list|()
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|abinding
operator|.
name|getRecipientToken
argument_list|()
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|abinding
operator|.
name|getInitiatorToken
argument_list|()
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|abinding
operator|.
name|getRecipientToken
argument_list|()
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|action
return|;
block|}
name|String
name|assertSymetricBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|action
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|SymmetricBinding
name|abinding
init|=
operator|(
name|SymmetricBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|abinding
operator|.
name|getProtectionOrder
argument_list|()
operator|==
name|SPConstants
operator|.
name|ProtectionOrder
operator|.
name|EncryptBeforeSigning
condition|)
block|{
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Signature"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Encrypt"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Encrypt"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
literal|"Signature"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|Object
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
decl_stmt|;
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
decl_stmt|;
if|if
condition|(
name|abinding
operator|.
name|getProtectionToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|s
operator|=
name|e
expr_stmt|;
block|}
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
literal|"SignaturePropRefId"
argument_list|,
literal|"SigRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"SigRefId"
argument_list|,
name|getProps
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"decryptionPropRefId"
argument_list|,
literal|"DecRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"DecRefId"
argument_list|,
name|getProps
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|put
argument_list|(
literal|"SignaturePropRefId"
argument_list|,
literal|"SigRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"SigRefId"
argument_list|,
name|getProps
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"decryptionPropRefId"
argument_list|,
literal|"DecRefId"
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"DecRefId"
argument_list|,
name|getProps
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|abinding
operator|.
name|getEncryptionToken
argument_list|()
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|abinding
operator|.
name|getSignatureToken
argument_list|()
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|abinding
operator|.
name|getProtectionToken
argument_list|()
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
expr_stmt|;
name|policyAsserted
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|action
return|;
block|}
name|void
name|assertWSS11
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|WSS11
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Wss11
name|wss11
init|=
operator|(
name|Wss11
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ENABLE_SIGNATURE_CONFIRMATION
argument_list|,
name|wss11
operator|.
name|isRequireSignatureConfirmation
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|PolicyAssertion
name|findAndAssertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|n
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|AssertionInfo
name|ai
init|=
name|ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|ai
operator|.
name|getAssertion
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|String
name|assertSupportingTokens
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|String
name|action
parameter_list|,
name|QName
name|n
parameter_list|)
block|{
name|SupportingToken
name|sp
init|=
operator|(
name|SupportingToken
operator|)
name|findAndAssertPolicy
argument_list|(
name|aim
argument_list|,
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|sp
operator|!=
literal|null
condition|)
block|{
name|action
operator|=
name|doTokens
argument_list|(
name|sp
operator|.
name|getTokens
argument_list|()
argument_list|,
name|action
argument_list|,
name|aim
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|action
return|;
block|}
specifier|protected
name|void
name|checkPolicies
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|RequestData
name|data
parameter_list|)
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
name|String
name|action
init|=
name|getString
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|action
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
condition|)
block|{
name|action
operator|=
name|addToAction
argument_list|(
name|action
argument_list|,
name|WSHandlerConstants
operator|.
name|TIMESTAMP
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|LAYOUT
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|TRANSPORT_BINDING
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertAsymetricBinding
argument_list|(
name|aim
argument_list|,
name|action
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSymetricBinding
argument_list|(
name|aim
argument_list|,
name|action
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|action
operator|=
name|assertSupportingTokens
argument_list|(
name|aim
argument_list|,
name|message
argument_list|,
name|action
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
name|assertWSS11
argument_list|(
name|aim
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|WSS10
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|TRUST_13
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP11Constants
operator|.
name|TRUST_10
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|action
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|doTokens
parameter_list|(
name|List
argument_list|<
name|Token
argument_list|>
name|tokens
parameter_list|,
name|String
name|action
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|msg
parameter_list|)
block|{
for|for
control|(
name|Token
name|token
range|:
name|tokens
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|UsernameToken
condition|)
block|{
if|if
condition|(
operator|!
name|action
operator|.
name|contains
argument_list|(
name|WSHandlerConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
operator|&&
operator|!
name|isRequestor
argument_list|(
name|msg
argument_list|)
condition|)
block|{
name|action
operator|=
name|WSHandlerConstants
operator|.
name|USERNAME_TOKEN
operator|+
literal|" "
operator|+
name|action
expr_stmt|;
block|}
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais2
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|USERNAME_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais2
operator|!=
literal|null
operator|&&
operator|!
name|ais2
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai2
range|:
name|ais2
control|)
block|{
if|if
condition|(
name|ai2
operator|.
name|getAssertion
argument_list|()
operator|==
name|token
condition|)
block|{
name|ai2
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais2
init|=
name|aim
operator|.
name|get
argument_list|(
name|token
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais2
operator|!=
literal|null
operator|&&
operator|!
name|ais2
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai2
range|:
name|ais2
control|)
block|{
if|if
condition|(
name|ai2
operator|.
name|getAssertion
argument_list|()
operator|==
name|token
condition|)
block|{
name|ai2
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|action
return|;
block|}
block|}
end_class

end_unit


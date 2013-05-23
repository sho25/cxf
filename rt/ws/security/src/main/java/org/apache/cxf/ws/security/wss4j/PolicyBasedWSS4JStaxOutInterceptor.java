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
name|Collections
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
name|Properties
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
name|endpoint
operator|.
name|Endpoint
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|wss4j
operator|.
name|policyhandlers
operator|.
name|StaxTransportBindingHandler
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
name|WSHandlerConstants
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
name|wss4j
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
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PolicyBasedWSS4JStaxOutInterceptor
extends|extends
name|WSS4JStaxOutInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|PolicyBasedWSS4JStaxOutInterceptor
name|INSTANCE
init|=
operator|new
name|PolicyBasedWSS4JStaxOutInterceptor
argument_list|()
decl_stmt|;
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
name|PolicyBasedWSS4JStaxOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PolicyBasedWSS4JStaxOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
throws|throws
name|Fault
block|{
name|AssertionInfoMap
name|aim
init|=
name|msg
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|enableStax
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|aim
operator|!=
literal|null
operator|&&
name|enableStax
condition|)
block|{
name|getProperties
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
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
name|URL
name|propsURL
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
name|propsURL
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|InputStream
name|ins
init|=
name|propsURL
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
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
specifier|private
name|URL
name|getPropertiesFileURL
parameter_list|(
name|Object
name|o
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
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
name|url
operator|=
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
expr_stmt|;
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
operator|==
literal|null
condition|)
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|url
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Do nothing
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
return|return
operator|(
name|URL
operator|)
name|o
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|getAllAssertionsByLocalname
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|String
name|localname
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp11Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|sp12Ais
init|=
name|aim
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|localname
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|sp11Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp11Ais
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|sp12Ais
operator|!=
literal|null
operator|&&
operator|!
name|sp12Ais
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
operator|new
name|HashSet
argument_list|<
name|AssertionInfo
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|sp11Ais
operator|!=
literal|null
condition|)
block|{
name|ais
operator|.
name|addAll
argument_list|(
name|sp11Ais
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sp12Ais
operator|!=
literal|null
condition|)
block|{
name|ais
operator|.
name|addAll
argument_list|(
name|sp12Ais
argument_list|)
expr_stmt|;
block|}
return|return
name|ais
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
specifier|private
name|void
name|checkAsymmetricBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
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
name|SIGNATURE_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|encrCrypto
init|=
name|getEncryptionCrypto
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|signCrypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
else|else
block|{
name|signCrypto
operator|=
name|getSignatureCrypto
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ENC_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|signCrypto
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encrCrypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|encrCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|encrCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
operator|(
name|Crypto
operator|)
name|encrCrypto
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
operator|(
name|Crypto
operator|)
name|signCrypto
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkTransportBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
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
name|SIGNATURE_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|encrCrypto
init|=
name|getEncryptionCrypto
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|signCrypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
else|else
block|{
name|signCrypto
operator|=
name|getSignatureCrypto
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ENC_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|signCrypto
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encrCrypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|encrCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|encrCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
operator|(
name|Crypto
operator|)
name|encrCrypto
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|signCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
operator|(
name|Crypto
operator|)
name|signCrypto
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkSymmetricBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
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
name|SIGNATURE_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|encrCrypto
init|=
name|getEncryptionCrypto
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|signCrypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
else|else
block|{
name|signCrypto
operator|=
name|getSignatureCrypto
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
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
name|Crypto
name|crypto
init|=
name|encrCrypto
decl_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|signCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
block|}
name|crypto
operator|=
name|signCrypto
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
name|encrCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ENC_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Crypto
name|crypto
init|=
name|signCrypto
decl_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
block|}
name|crypto
operator|=
name|encrCrypto
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
name|signCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ENC_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|crypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Crypto
name|getEncryptionCrypto
parameter_list|(
name|Object
name|e
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Crypto
name|encrCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|Crypto
condition|)
block|{
name|encrCrypto
operator|=
operator|(
name|Crypto
operator|)
name|e
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|URL
name|propsURL
init|=
name|getPropertiesFileURL
argument_list|(
name|e
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
name|getProps
argument_list|(
name|e
argument_list|,
name|propsURL
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot find Crypto Encryption properties: "
operator|+
name|e
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|(
literal|"Cannot find Crypto Encryption properties: "
operator|+
name|e
argument_list|)
decl_stmt|;
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
name|encrCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|EndpointInfo
name|info
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|info
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|,
name|encrCrypto
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|encrCrypto
return|;
block|}
specifier|private
name|Crypto
name|getSignatureCrypto
parameter_list|(
name|Object
name|s
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|s
operator|instanceof
name|Crypto
condition|)
block|{
name|signCrypto
operator|=
operator|(
name|Crypto
operator|)
name|s
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|URL
name|propsURL
init|=
name|getPropertiesFileURL
argument_list|(
name|s
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
name|getProps
argument_list|(
name|s
argument_list|,
name|propsURL
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot find Crypto Signature properties: "
operator|+
name|s
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|(
literal|"Cannot find Crypto Signature properties: "
operator|+
name|s
argument_list|)
decl_stmt|;
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
name|signCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|EndpointInfo
name|info
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|info
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|signCrypto
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|signCrypto
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|configureProperties
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|AssertionInfoMap
name|aim
init|=
name|msg
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|checkAsymmetricBinding
argument_list|(
name|aim
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|checkSymmetricBinding
argument_list|(
name|aim
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|checkTransportBinding
argument_list|(
name|aim
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
operator|new
name|StaxTransportBindingHandler
argument_list|(
name|getProperties
argument_list|()
argument_list|,
name|msg
argument_list|)
operator|.
name|handleBinding
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|configureProperties
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


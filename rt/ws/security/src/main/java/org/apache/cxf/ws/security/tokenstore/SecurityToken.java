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
name|tokenstore
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|DatatypeConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|DatatypeFactory
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
name|helpers
operator|.
name|DOMUtils
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
name|staxutils
operator|.
name|StaxUtils
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
name|staxutils
operator|.
name|W3CDOMStreamWriter
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
name|components
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
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|token
operator|.
name|Reference
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|SecurityToken
block|{
specifier|public
enum|enum
name|State
block|{
name|UNKNOWN
block|,
name|ISSUED
block|,
name|EXPIRED
block|,
name|CANCELLED
block|,
name|RENEWED
block|}
empty_stmt|;
comment|/**      * Token identifier      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * Current state of the token      */
specifier|private
name|State
name|state
init|=
name|State
operator|.
name|UNKNOWN
decl_stmt|;
comment|/**      * The actual token in its current state      */
specifier|private
name|Element
name|token
decl_stmt|;
comment|/**      * The token in its previous state      */
specifier|private
name|Element
name|previousToken
decl_stmt|;
comment|/**      * The RequestedAttachedReference element      * NOTE : The oasis-200401-wss-soap-message-security-1.0 spec allows       * an extensibility mechanism for wsse:SecurityTokenReference and       * wsse:Reference. Hence we cannot limit to the       * wsse:SecurityTokenReference\wsse:Reference case and only hold the URI and       * the ValueType values.      */
specifier|private
name|Element
name|attachedReference
decl_stmt|;
comment|/**      * The RequestedUnattachedReference element      * NOTE : The oasis-200401-wss-soap-message-security-1.0 spec allows       * an extensibility mechanism for wsse:SecurityTokenReference and       * wsse:Reference. Hence we cannot limit to the       * wsse:SecurityTokenReference\wsse:Reference case and only hold the URI and       * the ValueType values.      */
specifier|private
name|Element
name|unattachedReference
decl_stmt|;
comment|/**      * A bag to hold any other properties      */
specifier|private
name|Properties
name|properties
decl_stmt|;
comment|/**      * A flag to assist the TokenStorage      */
specifier|private
name|boolean
name|changed
decl_stmt|;
comment|/**      * The secret associated with the Token      */
specifier|private
name|byte
index|[]
name|secret
decl_stmt|;
comment|/**      * Created time      */
specifier|private
name|Calendar
name|created
decl_stmt|;
comment|/**      * Expiration time      */
specifier|private
name|Calendar
name|expires
decl_stmt|;
comment|/**      * Issuer end point address      */
specifier|private
name|String
name|issuerAddress
decl_stmt|;
comment|/**      * If an encrypted key, this contains the sha1 for the key      */
specifier|private
name|String
name|encrKeySha1Value
decl_stmt|;
comment|/**      * The tokenType      */
specifier|private
name|String
name|tokenType
decl_stmt|;
specifier|private
name|X509Certificate
name|x509cert
decl_stmt|;
specifier|private
name|Crypto
name|crypto
decl_stmt|;
specifier|public
name|SecurityToken
parameter_list|()
block|{              }
specifier|public
name|SecurityToken
parameter_list|(
name|String
name|id
parameter_list|,
name|Calendar
name|created
parameter_list|,
name|Calendar
name|expires
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|created
operator|=
name|created
expr_stmt|;
name|this
operator|.
name|expires
operator|=
name|expires
expr_stmt|;
block|}
specifier|public
name|SecurityToken
parameter_list|(
name|String
name|id
parameter_list|,
name|Element
name|tokenElem
parameter_list|,
name|Calendar
name|created
parameter_list|,
name|Calendar
name|expires
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|token
operator|=
name|cloneElement
argument_list|(
name|tokenElem
argument_list|)
expr_stmt|;
name|this
operator|.
name|created
operator|=
name|created
expr_stmt|;
name|this
operator|.
name|expires
operator|=
name|expires
expr_stmt|;
block|}
specifier|public
name|SecurityToken
parameter_list|(
name|String
name|id
parameter_list|,
name|Element
name|tokenElem
parameter_list|,
name|Element
name|lifetimeElem
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|token
operator|=
name|cloneElement
argument_list|(
name|tokenElem
argument_list|)
expr_stmt|;
name|this
operator|.
name|processLifeTime
argument_list|(
name|lifetimeElem
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Element
name|cloneElement
parameter_list|(
name|Element
name|el
parameter_list|)
block|{
try|try
block|{
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|setNsRepairing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|el
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|el
return|;
block|}
comment|/**      * @param lifetimeElem      * @throws TrustException       */
specifier|private
name|void
name|processLifeTime
parameter_list|(
name|Element
name|lifetimeElem
parameter_list|)
block|{
try|try
block|{
name|DatatypeFactory
name|factory
init|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Element
name|createdElem
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|lifetimeElem
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|,
name|WSConstants
operator|.
name|CREATED_LN
argument_list|)
decl_stmt|;
name|this
operator|.
name|created
operator|=
name|factory
operator|.
name|newXMLGregorianCalendar
argument_list|(
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|createdElem
argument_list|)
argument_list|)
operator|.
name|toGregorianCalendar
argument_list|()
expr_stmt|;
name|Element
name|expiresElem
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|lifetimeElem
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|,
name|WSConstants
operator|.
name|EXPIRES_LN
argument_list|)
decl_stmt|;
name|this
operator|.
name|expires
operator|=
name|factory
operator|.
name|newXMLGregorianCalendar
argument_list|(
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|expiresElem
argument_list|)
argument_list|)
operator|.
name|toGregorianCalendar
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DatatypeConfigurationException
name|e
parameter_list|)
block|{
comment|//shouldn't happen
block|}
block|}
comment|/**      * @return Returns the changed.      */
specifier|public
name|boolean
name|isChanged
parameter_list|()
block|{
return|return
name|changed
return|;
block|}
comment|/**      * @param chnaged The changed to set.      */
specifier|public
name|void
name|setChanged
parameter_list|(
name|boolean
name|chnaged
parameter_list|)
block|{
name|this
operator|.
name|changed
operator|=
name|chnaged
expr_stmt|;
block|}
comment|/**      * @return Returns the properties.      */
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * @param properties The properties to set.      */
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
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
comment|/**      * @return Returns the state.      */
specifier|public
name|State
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
comment|/**      * @param state The state to set.      */
specifier|public
name|void
name|setState
parameter_list|(
name|State
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
comment|/**      * @return Returns the token.      */
specifier|public
name|Element
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
comment|/**      * @param token The token to set.      */
specifier|public
name|void
name|setToken
parameter_list|(
name|Element
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
comment|/**      * @return Returns the id.      */
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * @return Returns the presivousToken.      */
specifier|public
name|Element
name|getPreviousToken
parameter_list|()
block|{
return|return
name|previousToken
return|;
block|}
comment|/**      * @param presivousToken The presivousToken to set.      */
specifier|public
name|void
name|setPreviousToken
parameter_list|(
name|Element
name|previousToken
parameter_list|)
block|{
name|this
operator|.
name|previousToken
operator|=
name|cloneElement
argument_list|(
name|previousToken
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the secret.      */
specifier|public
name|byte
index|[]
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
comment|/**      * @param secret The secret to set.      */
specifier|public
name|void
name|setSecret
parameter_list|(
name|byte
index|[]
name|secret
parameter_list|)
block|{
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
block|}
comment|/**      * @return Returns the attachedReference.      */
specifier|public
name|Element
name|getAttachedReference
parameter_list|()
block|{
return|return
name|attachedReference
return|;
block|}
comment|/**      * @param attachedReference The attachedReference to set.      */
specifier|public
name|void
name|setAttachedReference
parameter_list|(
name|Element
name|attachedReference
parameter_list|)
block|{
if|if
condition|(
name|attachedReference
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|attachedReference
operator|=
name|cloneElement
argument_list|(
name|attachedReference
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return Returns the unattachedReference.      */
specifier|public
name|Element
name|getUnattachedReference
parameter_list|()
block|{
return|return
name|unattachedReference
return|;
block|}
comment|/**      * @param unattachedReference The unattachedReference to set.      */
specifier|public
name|void
name|setUnattachedReference
parameter_list|(
name|Element
name|unattachedReference
parameter_list|)
block|{
if|if
condition|(
name|unattachedReference
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|unattachedReference
operator|=
name|cloneElement
argument_list|(
name|unattachedReference
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return Returns the created.      */
specifier|public
name|Calendar
name|getCreated
parameter_list|()
block|{
return|return
name|created
return|;
block|}
comment|/**      * @return Returns the expires.      */
specifier|public
name|Calendar
name|getExpires
parameter_list|()
block|{
return|return
name|expires
return|;
block|}
comment|/**      * @param expires The expires to set.      */
specifier|public
name|void
name|setExpires
parameter_list|(
name|Calendar
name|expires
parameter_list|)
block|{
name|this
operator|.
name|expires
operator|=
name|expires
expr_stmt|;
block|}
specifier|public
name|String
name|getIssuerAddress
parameter_list|()
block|{
return|return
name|issuerAddress
return|;
block|}
specifier|public
name|void
name|setIssuerAddress
parameter_list|(
name|String
name|issuerAddress
parameter_list|)
block|{
name|this
operator|.
name|issuerAddress
operator|=
name|issuerAddress
expr_stmt|;
block|}
comment|/**      * @param sha SHA1 of the encrypted key      */
specifier|public
name|void
name|setSHA1
parameter_list|(
name|String
name|sha
parameter_list|)
block|{
name|this
operator|.
name|encrKeySha1Value
operator|=
name|sha
expr_stmt|;
block|}
comment|/**       * @return SHA1 value of the encrypted key       */
specifier|public
name|String
name|getSHA1
parameter_list|()
block|{
return|return
name|encrKeySha1Value
return|;
block|}
specifier|public
name|String
name|getTokenType
parameter_list|()
block|{
return|return
name|tokenType
return|;
block|}
specifier|public
name|void
name|setTokenType
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|tokenType
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|String
name|getWsuId
parameter_list|()
block|{
name|Element
name|elem
init|=
name|getAttachedReference
argument_list|()
decl_stmt|;
if|if
condition|(
name|elem
operator|!=
literal|null
condition|)
block|{
name|String
name|t
init|=
name|getIdFromSTR
argument_list|(
name|elem
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
return|return
name|t
return|;
block|}
block|}
name|elem
operator|=
name|getUnattachedReference
argument_list|()
expr_stmt|;
if|if
condition|(
name|elem
operator|!=
literal|null
condition|)
block|{
name|String
name|t
init|=
name|getIdFromSTR
argument_list|(
name|elem
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
return|return
name|t
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|getIdFromSTR
parameter_list|(
name|Element
name|str
parameter_list|)
block|{
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|str
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
literal|"KeyInfo"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|SIG_NS
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|child
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Reference
operator|.
name|TOKEN
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|Reference
operator|.
name|TOKEN
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|child
operator|.
name|getAttribute
argument_list|(
literal|"URI"
argument_list|)
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setX509Certificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|Crypto
name|cpt
parameter_list|)
block|{
name|x509cert
operator|=
name|cert
expr_stmt|;
name|crypto
operator|=
name|cpt
expr_stmt|;
block|}
specifier|public
name|X509Certificate
name|getX509Certificate
parameter_list|()
block|{
return|return
name|x509cert
return|;
block|}
specifier|public
name|Crypto
name|getCrypto
parameter_list|()
block|{
return|return
name|crypto
return|;
block|}
block|}
end_class

end_unit


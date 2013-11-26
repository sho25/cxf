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
name|rs
operator|.
name|security
operator|.
name|saml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|List
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|CryptoLoader
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
name|rs
operator|.
name|security
operator|.
name|common
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
operator|.
name|Claim
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
operator|.
name|Claims
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
operator|.
name|Subject
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
name|saml
operator|.
name|SAMLCallback
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
name|saml
operator|.
name|SAMLUtil
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
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeStatement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|NameID
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xml
operator|.
name|XMLObject
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SAMLUtils
block|{
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
name|SAMLUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SAMLUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|Subject
name|getSubject
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|assertionW
parameter_list|)
block|{
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Subject
name|s
init|=
name|assertionW
operator|.
name|getSaml2
argument_list|()
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|NameID
name|nameId
init|=
name|s
operator|.
name|getNameID
argument_list|()
decl_stmt|;
name|subject
operator|.
name|setNameQualifier
argument_list|(
name|nameId
operator|.
name|getNameQualifier
argument_list|()
argument_list|)
expr_stmt|;
comment|// if format is transient then we may need to use STSClient
comment|// to request an alternate name from IDP
name|subject
operator|.
name|setNameFormat
argument_list|(
name|nameId
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|subject
operator|.
name|setName
argument_list|(
name|nameId
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|subject
operator|.
name|setSpId
argument_list|(
name|nameId
operator|.
name|getSPProvidedID
argument_list|()
argument_list|)
expr_stmt|;
name|subject
operator|.
name|setSpQualifier
argument_list|(
name|nameId
operator|.
name|getSPNameQualifier
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|subject
return|;
block|}
specifier|public
specifier|static
name|Claims
name|getClaims
parameter_list|(
name|SamlAssertionWrapper
name|assertionW
parameter_list|)
block|{
comment|// Should we just do a simple DOM parsing without even relying on
comment|// OpenSaml
name|List
argument_list|<
name|Claim
argument_list|>
name|claims
init|=
operator|new
name|ArrayList
argument_list|<
name|Claim
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AttributeStatement
argument_list|>
name|statements
init|=
name|assertionW
operator|.
name|getSaml2
argument_list|()
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
for|for
control|(
name|AttributeStatement
name|as
range|:
name|statements
control|)
block|{
for|for
control|(
name|Attribute
name|atr
range|:
name|as
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|Claim
name|claim
init|=
operator|new
name|Claim
argument_list|()
decl_stmt|;
name|claim
operator|.
name|setName
argument_list|(
name|atr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setNameFormat
argument_list|(
name|atr
operator|.
name|getNameFormat
argument_list|()
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setFriendlyName
argument_list|(
name|atr
operator|.
name|getFriendlyName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|XMLObject
name|o
range|:
name|atr
operator|.
name|getAttributeValues
argument_list|()
control|)
block|{
name|String
name|attrValue
init|=
name|o
operator|.
name|getDOM
argument_list|()
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|add
argument_list|(
name|attrValue
argument_list|)
expr_stmt|;
block|}
name|claims
operator|.
name|add
argument_list|(
name|claim
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|Claims
argument_list|(
name|claims
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SamlAssertionWrapper
name|createAssertion
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|CallbackHandler
name|handler
init|=
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|message
argument_list|,
name|SAMLUtils
operator|.
name|class
argument_list|,
name|SecurityConstants
operator|.
name|SAML_CALLBACK_HANDLER
argument_list|)
decl_stmt|;
return|return
name|createAssertion
argument_list|(
name|message
argument_list|,
name|handler
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SamlAssertionWrapper
name|createAssertion
parameter_list|(
name|Message
name|message
parameter_list|,
name|CallbackHandler
name|handler
parameter_list|)
throws|throws
name|Fault
block|{
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|handler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
try|try
block|{
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
if|if
condition|(
name|samlCallback
operator|.
name|isSignAssertion
argument_list|()
condition|)
block|{
comment|//--- This code will be moved to a common utility class
name|Crypto
name|crypto
init|=
operator|new
name|CryptoLoader
argument_list|()
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|SecurityUtils
operator|.
name|getUserName
argument_list|(
name|message
argument_list|,
name|crypto
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|user
argument_list|)
condition|)
block|{
return|return
name|assertion
return|;
block|}
name|String
name|password
init|=
name|SecurityUtils
operator|.
name|getPassword
argument_list|(
name|message
argument_list|,
name|user
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|,
name|SAMLUtils
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|signAssertion
argument_list|(
name|user
argument_list|,
name|password
argument_list|,
name|crypto
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|assertion
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|RuntimeException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|", stacktrace: "
operator|+
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|SamlAssertionWrapper
name|createAssertion
parameter_list|(
name|CallbackHandler
name|handler
parameter_list|,
name|SelfSignInfo
name|info
parameter_list|)
throws|throws
name|Fault
block|{
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|handler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
try|try
block|{
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|signAssertion
argument_list|(
name|info
operator|.
name|getUser
argument_list|()
argument_list|,
name|info
operator|.
name|getPassword
argument_list|()
argument_list|,
name|info
operator|.
name|getCrypto
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|assertion
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|RuntimeException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|", stacktrace: "
operator|+
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
class|class
name|SelfSignInfo
block|{
specifier|private
name|Crypto
name|crypto
decl_stmt|;
specifier|private
name|String
name|user
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|public
name|SelfSignInfo
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|crypto
operator|=
name|crypto
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
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
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


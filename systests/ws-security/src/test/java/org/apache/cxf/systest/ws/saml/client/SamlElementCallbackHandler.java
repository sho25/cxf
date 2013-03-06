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
name|systest
operator|.
name|ws
operator|.
name|saml
operator|.
name|client
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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

begin_comment
comment|/**  * A CallbackHandler instance that is used by the STS to mock up a SAML Attribute Assertion. This  * particular CallbackHandler creates the SAML Assertion by delegating it to the standard  * SamlCallbackHandler, and then just sets it on the SAMLCallback as a DOM Element. Essentially,  * this is a test that it's possible to set a DOM Element on the SAMLCallback and have it included  * in the request.  */
end_comment

begin_class
specifier|public
class|class
name|SamlElementCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|boolean
name|saml2
init|=
literal|true
decl_stmt|;
specifier|public
name|SamlElementCallbackHandler
parameter_list|()
block|{
comment|//
block|}
specifier|public
name|SamlElementCallbackHandler
parameter_list|(
name|boolean
name|saml2
parameter_list|)
block|{
name|this
operator|.
name|saml2
operator|=
name|saml2
expr_stmt|;
block|}
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|SAMLCallback
condition|)
block|{
name|SAMLCallback
name|callback
init|=
operator|(
name|SAMLCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
name|Element
name|assertionElement
decl_stmt|;
try|try
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|assertionElement
operator|=
name|getSAMLAssertion
argument_list|(
name|doc
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
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|callback
operator|.
name|setAssertionElement
argument_list|(
name|assertionElement
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Mock up a SAML Assertion by using another SAMLCallbackHandler      * @throws Exception       */
specifier|private
name|Element
name|getSAMLAssertion
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
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
operator|new
name|SamlCallbackHandler
argument_list|(
name|saml2
argument_list|)
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|assertionWrapper
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
return|return
name|assertionWrapper
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
return|;
block|}
block|}
end_class

end_unit


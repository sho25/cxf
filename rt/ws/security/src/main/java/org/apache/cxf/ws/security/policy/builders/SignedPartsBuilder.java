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
name|policy
operator|.
name|builders
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|AssertionBuilder
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
name|Header
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
name|SignedEncryptedParts
import|;
end_import

begin_class
specifier|public
class|class
name|SignedPartsBuilder
implements|implements
name|AssertionBuilder
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|QName
argument_list|>
name|KNOWN_ELEMENTS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|SP11Constants
operator|.
name|SIGNED_PARTS
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|)
decl_stmt|;
specifier|public
name|List
argument_list|<
name|QName
argument_list|>
name|getKnownElements
parameter_list|()
block|{
return|return
name|KNOWN_ELEMENTS
return|;
block|}
specifier|public
name|PolicyAssertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|SPConstants
name|consts
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|?
name|SP11Constants
operator|.
name|INSTANCE
else|:
name|SP12Constants
operator|.
name|INSTANCE
decl_stmt|;
name|SignedEncryptedParts
name|signedEncryptedParts
init|=
operator|new
name|SignedEncryptedParts
argument_list|(
literal|true
argument_list|,
name|consts
argument_list|)
decl_stmt|;
name|Node
name|nd
init|=
name|element
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nd
operator|instanceof
name|Element
condition|)
block|{
name|processElement
argument_list|(
operator|(
name|Element
operator|)
name|nd
argument_list|,
name|signedEncryptedParts
argument_list|)
expr_stmt|;
block|}
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
return|return
name|signedEncryptedParts
return|;
block|}
specifier|private
name|void
name|processElement
parameter_list|(
name|Element
name|element
parameter_list|,
name|SignedEncryptedParts
name|parent
parameter_list|)
block|{
if|if
condition|(
literal|"Header"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|nameAttribute
init|=
name|element
operator|.
name|getAttribute
argument_list|(
name|SPConstants
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameAttribute
operator|==
literal|null
condition|)
block|{
name|nameAttribute
operator|=
literal|""
expr_stmt|;
block|}
name|String
name|namespaceAttribute
init|=
name|element
operator|.
name|getAttribute
argument_list|(
name|SPConstants
operator|.
name|NAMESPACE
argument_list|)
decl_stmt|;
name|parent
operator|.
name|addHeader
argument_list|(
operator|new
name|Header
argument_list|(
name|nameAttribute
argument_list|,
name|namespaceAttribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"Body"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|PolicyAssertion
name|buildCompatible
parameter_list|(
name|PolicyAssertion
name|a
parameter_list|,
name|PolicyAssertion
name|b
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit


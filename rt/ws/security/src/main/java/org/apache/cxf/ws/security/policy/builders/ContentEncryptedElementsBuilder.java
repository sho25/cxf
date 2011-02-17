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
name|Attr
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
name|NamedNodeMap
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
name|ContentEncryptedElements
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|AssertionBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|AssertionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|ContentEncryptedElementsBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|QName
name|KNOWN_ELEMENTS
index|[]
init|=
block|{
name|SP12Constants
operator|.
name|CONTENT_ENCRYPTED_ELEMENTS
block|}
decl_stmt|;
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
block|{
name|ContentEncryptedElements
name|contentEncryptedElements
init|=
operator|new
name|ContentEncryptedElements
argument_list|(
name|SP12Constants
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|String
name|attrXPathVersion
init|=
name|DOMUtils
operator|.
name|getAttribute
argument_list|(
name|element
argument_list|,
name|SP12Constants
operator|.
name|ATTR_XPATH_VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|attrXPathVersion
operator|!=
literal|null
condition|)
block|{
name|contentEncryptedElements
operator|.
name|setXPathVersion
argument_list|(
name|attrXPathVersion
argument_list|)
expr_stmt|;
block|}
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
name|contentEncryptedElements
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
name|contentEncryptedElements
return|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
name|KNOWN_ELEMENTS
return|;
block|}
specifier|private
name|void
name|processElement
parameter_list|(
name|Element
name|element
parameter_list|,
name|ContentEncryptedElements
name|parent
parameter_list|)
block|{
if|if
condition|(
name|SPConstants
operator|.
name|XPATH_EXPR
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
name|addXPathExpression
argument_list|(
name|DOMUtils
operator|.
name|getRawContent
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|addNamespaces
argument_list|(
name|element
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addNamespaces
parameter_list|(
name|Node
name|element
parameter_list|,
name|ContentEncryptedElements
name|parent
parameter_list|)
block|{
if|if
condition|(
name|element
operator|.
name|getParentNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addNamespaces
argument_list|(
name|element
operator|.
name|getParentNode
argument_list|()
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|element
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|element
decl_stmt|;
name|NamedNodeMap
name|map
init|=
name|el
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|map
operator|.
name|getLength
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Attr
name|attr
init|=
operator|(
name|Attr
operator|)
name|map
operator|.
name|item
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|attr
operator|.
name|getPrefix
argument_list|()
argument_list|)
condition|)
block|{
name|parent
operator|.
name|addDeclaredNamespaces
argument_list|(
name|attr
operator|.
name|getValue
argument_list|()
argument_list|,
name|attr
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit


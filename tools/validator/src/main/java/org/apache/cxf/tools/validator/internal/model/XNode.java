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
name|tools
operator|.
name|validator
operator|.
name|internal
operator|.
name|model
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
name|java
operator|.
name|util
operator|.
name|Stack
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
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_class
specifier|public
class|class
name|XNode
block|{
specifier|private
name|String
name|prefix
decl_stmt|;
specifier|private
name|QName
name|name
decl_stmt|;
specifier|private
name|String
name|attributeName
decl_stmt|;
specifier|private
name|String
name|attributeValue
decl_stmt|;
specifier|private
name|boolean
name|isDefaultAttributeValue
decl_stmt|;
specifier|private
name|XNode
name|parentNode
decl_stmt|;
specifier|private
name|XNode
name|failurePoint
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setFailurePoint
parameter_list|(
name|XNode
name|point
parameter_list|)
block|{
name|this
operator|.
name|failurePoint
operator|=
name|point
expr_stmt|;
block|}
specifier|public
name|XNode
name|getFailurePoint
parameter_list|()
block|{
return|return
name|this
operator|.
name|failurePoint
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
specifier|final
name|String
name|newPrefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|newPrefix
expr_stmt|;
block|}
specifier|public
name|QName
name|getQName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setQName
parameter_list|(
specifier|final
name|QName
name|newName
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|newName
expr_stmt|;
block|}
specifier|public
name|String
name|getAttributeName
parameter_list|()
block|{
return|return
name|attributeName
return|;
block|}
specifier|public
name|void
name|setAttributeName
parameter_list|(
specifier|final
name|String
name|newAttributeName
parameter_list|)
block|{
name|this
operator|.
name|attributeName
operator|=
name|newAttributeName
expr_stmt|;
block|}
specifier|public
name|String
name|getAttributeValue
parameter_list|()
block|{
return|return
name|attributeValue
return|;
block|}
specifier|public
name|void
name|setAttributeValue
parameter_list|(
specifier|final
name|String
name|newAttributeValue
parameter_list|)
block|{
name|this
operator|.
name|attributeValue
operator|=
name|newAttributeValue
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultAttributeValue
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|this
operator|.
name|isDefaultAttributeValue
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDefaultAttributeValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|isDefaultAttributeValue
return|;
block|}
specifier|public
name|XNode
name|getParentNode
parameter_list|()
block|{
return|return
name|parentNode
return|;
block|}
specifier|public
name|void
name|setParentNode
parameter_list|(
specifier|final
name|XNode
name|newParentNode
parameter_list|)
block|{
name|this
operator|.
name|parentNode
operator|=
name|newParentNode
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNSMap
parameter_list|()
block|{
return|return
name|nsMap
return|;
block|}
specifier|public
name|String
name|getText
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getAttributeValue
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPlainText
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Stack
argument_list|<
name|XNode
argument_list|>
name|parentNodes
init|=
name|getParentNodes
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|parentNodes
operator|.
name|empty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|parentNodes
operator|.
name|pop
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|nsMap
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getXPath
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|attributeName
argument_list|)
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|attributeValue
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
if|if
condition|(
name|isDefaultAttributeValue
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"not(@"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|attributeName
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|") or "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'@'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|attributeName
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"='"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|attributeValue
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|Stack
argument_list|<
name|XNode
argument_list|>
name|getParentNodes
parameter_list|()
block|{
name|Stack
argument_list|<
name|XNode
argument_list|>
name|parentNodes
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
name|XNode
name|pNode
init|=
name|getParentNode
argument_list|()
decl_stmt|;
while|while
condition|(
name|pNode
operator|!=
literal|null
condition|)
block|{
name|nsMap
operator|.
name|put
argument_list|(
name|pNode
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|pNode
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|parentNodes
operator|.
name|push
argument_list|(
name|pNode
argument_list|)
expr_stmt|;
name|pNode
operator|=
name|pNode
operator|.
name|getParentNode
argument_list|()
expr_stmt|;
block|}
return|return
name|parentNodes
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|Stack
argument_list|<
name|XNode
argument_list|>
name|parentNodes
init|=
name|getParentNodes
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|parentNodes
operator|.
name|empty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|parentNodes
operator|.
name|pop
argument_list|()
operator|.
name|getXPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|getXPath
argument_list|()
argument_list|)
expr_stmt|;
name|nsMap
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|matches
parameter_list|(
name|Element
name|el
parameter_list|)
block|{
if|if
condition|(
name|el
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|el
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|attributeName
argument_list|)
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|attributeValue
argument_list|)
condition|)
block|{
name|String
name|v
init|=
name|el
operator|.
name|getAttribute
argument_list|(
name|attributeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|attributeValue
operator|.
name|equals
argument_list|(
name|v
argument_list|)
operator|||
operator|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|v
argument_list|)
operator|&&
name|isDefaultAttributeValue
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|matches
parameter_list|(
name|Element
name|el
parameter_list|,
name|Stack
argument_list|<
name|XNode
argument_list|>
name|stack
parameter_list|)
block|{
if|if
condition|(
name|matches
argument_list|(
name|el
argument_list|)
condition|)
block|{
if|if
condition|(
name|stack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|XNode
name|next
init|=
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Node
name|nd
init|=
name|el
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
name|el
operator|=
operator|(
name|Element
operator|)
name|nd
expr_stmt|;
if|if
condition|(
name|next
operator|.
name|matches
argument_list|(
name|el
argument_list|,
name|stack
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|stack
operator|.
name|push
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|matches
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|Stack
argument_list|<
name|XNode
argument_list|>
name|nodes
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
name|nodes
operator|.
name|push
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|XNode
name|pNode
init|=
name|getParentNode
argument_list|()
decl_stmt|;
while|while
condition|(
name|pNode
operator|!=
literal|null
condition|)
block|{
name|nodes
operator|.
name|push
argument_list|(
name|pNode
argument_list|)
expr_stmt|;
name|pNode
operator|=
name|pNode
operator|.
name|getParentNode
argument_list|()
expr_stmt|;
block|}
name|pNode
operator|=
name|nodes
operator|.
name|pop
argument_list|()
expr_stmt|;
return|return
name|pNode
operator|.
name|matches
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|nodes
argument_list|)
return|;
block|}
block|}
end_class

end_unit


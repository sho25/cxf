begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *   */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|util
operator|.
name|stax
package|;
end_package

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
name|Iterator
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
name|NamespaceContext
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

begin_class
specifier|public
class|class
name|W3CNamespaceContext
implements|implements
name|NamespaceContext
block|{
specifier|private
name|Element
name|currentNode
decl_stmt|;
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|String
name|name
init|=
name|prefix
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
literal|"xmlns"
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
literal|"xmlns:"
operator|+
name|prefix
expr_stmt|;
block|}
return|return
name|getNamespaceURI
argument_list|(
name|currentNode
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|private
name|String
name|getNamespaceURI
parameter_list|(
name|Element
name|e
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Attr
name|attr
init|=
name|e
operator|.
name|getAttributeNode
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|==
literal|null
condition|)
block|{
name|Node
name|n
init|=
name|e
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|instanceof
name|Element
operator|&&
name|n
operator|!=
name|e
condition|)
block|{
return|return
name|getNamespaceURI
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|name
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
name|attr
operator|.
name|getValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|getPrefix
argument_list|(
name|currentNode
argument_list|,
name|uri
argument_list|)
return|;
block|}
specifier|private
name|String
name|getPrefix
parameter_list|(
name|Element
name|e
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|NamedNodeMap
name|attributes
init|=
name|e
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
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
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Attr
name|a
init|=
operator|(
name|Attr
operator|)
name|attributes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|val
init|=
name|a
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
name|val
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|a
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
return|;
block|}
block|}
block|}
block|}
name|Node
name|n
init|=
name|e
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|instanceof
name|Element
operator|&&
name|n
operator|!=
name|e
condition|)
block|{
return|return
name|getPrefix
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|,
name|uri
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getPrefixes
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|getPrefix
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|prefixes
operator|.
name|add
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
return|return
name|prefixes
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Element
name|getElement
parameter_list|()
block|{
return|return
name|currentNode
return|;
block|}
specifier|public
name|void
name|setElement
parameter_list|(
name|Element
name|cn
parameter_list|)
block|{
name|this
operator|.
name|currentNode
operator|=
name|cn
expr_stmt|;
block|}
block|}
end_class

end_unit


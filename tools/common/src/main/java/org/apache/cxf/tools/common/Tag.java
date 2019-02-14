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
name|common
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
name|HashMap
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
specifier|public
class|class
name|Tag
block|{
name|QName
name|name
decl_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|attributes
decl_stmt|;
name|String
name|text
decl_stmt|;
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
decl_stmt|;
name|Tag
name|parent
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ignoreAttr
decl_stmt|;
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIgnoreAttr
parameter_list|()
block|{
if|if
condition|(
name|ignoreAttr
operator|==
literal|null
condition|)
block|{
name|ignoreAttr
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|ignoreAttr
return|;
block|}
specifier|public
name|Tag
name|getParent
parameter_list|()
block|{
return|return
name|this
operator|.
name|parent
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|Tag
name|nTag
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|nTag
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Tag
argument_list|>
name|getTags
parameter_list|()
block|{
if|if
condition|(
name|tags
operator|==
literal|null
condition|)
block|{
name|tags
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|tags
return|;
block|}
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|nText
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|nText
expr_stmt|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|QName
name|nName
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|nName
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
block|{
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|attributes
return|;
block|}
specifier|private
name|String
name|createIndent
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|String
name|indent
init|=
literal|"    "
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|indent
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
name|String
name|formatAttribute
parameter_list|(
specifier|final
name|Tag
name|tag
parameter_list|)
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
name|tag
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|attr
range|:
name|tag
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|attr
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\" "
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
return|;
block|}
specifier|private
name|String
name|formatTag
parameter_list|(
name|Tag
name|tag
parameter_list|,
name|int
name|indent
parameter_list|)
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
name|createIndent
argument_list|(
name|indent
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|indent
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|formatAttribute
argument_list|(
name|tag
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
if|if
condition|(
name|tag
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" ("
operator|+
name|tag
operator|.
name|getParent
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|tag
operator|.
name|getTags
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|indent
operator|++
expr_stmt|;
for|for
control|(
name|Tag
name|subTag
range|:
name|tag
operator|.
name|getTags
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|formatTag
argument_list|(
name|subTag
argument_list|,
name|indent
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|toString
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
name|formatTag
argument_list|(
name|this
argument_list|,
literal|0
argument_list|)
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
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|+
name|getAttributes
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Tag
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|object
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Tag
name|tag
init|=
operator|(
name|Tag
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|QName
name|attr
range|:
name|getAttributes
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|getIgnoreAttr
argument_list|()
operator|.
name|contains
argument_list|(
name|attr
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|||
name|getIgnoreAttr
argument_list|()
operator|.
name|contains
argument_list|(
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"@"
operator|+
name|attr
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|tag
operator|.
name|getAttributes
argument_list|()
operator|.
name|containsKey
argument_list|(
name|attr
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|tag
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attr
argument_list|)
operator|.
name|equals
argument_list|(
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attr
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


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
name|jaxws
operator|.
name|context
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
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
name|Iterator
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|attachment
operator|.
name|AttachmentImpl
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
name|Attachment
import|;
end_import

begin_comment
comment|/**  * This is a package local attachments wrapper class to treat the jaxws attachments  * as CXF's attachments.  */
end_comment

begin_class
class|class
name|WrappedAttachments
implements|implements
name|Set
argument_list|<
name|Attachment
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|cache
decl_stmt|;
name|WrappedAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
parameter_list|)
block|{
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
name|this
operator|.
name|cache
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|attachments
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|attachments
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Attachment
condition|)
block|{
return|return
name|attachments
operator|.
name|containsKey
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|WrappedAttachmentsIterator
argument_list|(
name|attachments
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|attachments
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|a
parameter_list|)
block|{
name|T
index|[]
name|copy
init|=
name|a
operator|.
name|length
operator|==
name|attachments
operator|.
name|size
argument_list|()
condition|?
name|a
else|:
operator|(
name|T
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|a
operator|.
name|getClass
argument_list|()
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|entry
range|:
name|attachments
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Attachment
name|o
init|=
name|cache
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
operator|new
name|AttachmentImpl
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
name|copy
index|[
name|i
operator|++
index|]
operator|=
operator|(
name|T
operator|)
name|o
expr_stmt|;
block|}
return|return
name|copy
return|;
block|}
specifier|public
name|boolean
name|add
parameter_list|(
name|Attachment
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|attachments
operator|.
name|containsKey
argument_list|(
name|e
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|attachments
operator|.
name|put
argument_list|(
name|e
operator|.
name|getId
argument_list|()
argument_list|,
name|e
operator|.
name|getDataHandler
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|e
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Attachment
condition|)
block|{
name|cache
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|attachments
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
literal|null
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|boolean
name|b
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|o
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Attachment
operator|)
operator|&&
name|attachments
operator|.
name|containsKey
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|b
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
return|return
name|b
return|;
block|}
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Attachment
argument_list|>
name|c
parameter_list|)
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
extends|extends
name|Attachment
argument_list|>
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Attachment
name|o
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|attachments
operator|.
name|containsKey
argument_list|(
name|o
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|b
operator|=
literal|true
expr_stmt|;
name|attachments
operator|.
name|put
argument_list|(
name|o
operator|.
name|getId
argument_list|()
argument_list|,
name|o
operator|.
name|getDataHandler
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|o
operator|.
name|getId
argument_list|()
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|b
return|;
block|}
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|o
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Attachment
condition|)
block|{
name|ids
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|attachments
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|k
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ids
operator|.
name|contains
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|b
operator|=
literal|true
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|b
return|;
block|}
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|o
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Attachment
operator|&&
name|attachments
operator|.
name|containsKey
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|b
operator|=
literal|true
expr_stmt|;
name|attachments
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Attachment
operator|)
name|o
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|b
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|attachments
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|getAttachments
parameter_list|()
block|{
return|return
name|attachments
return|;
block|}
class|class
name|WrappedAttachmentsIterator
implements|implements
name|Iterator
argument_list|<
name|Attachment
argument_list|>
block|{
specifier|private
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
argument_list|>
name|iterator
decl_stmt|;
specifier|private
name|String
name|key
decl_stmt|;
name|WrappedAttachmentsIterator
parameter_list|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
argument_list|>
name|iterator
parameter_list|)
block|{
name|this
operator|.
name|iterator
operator|=
name|iterator
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|iterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|Attachment
name|next
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|e
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|key
operator|=
name|e
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|Attachment
name|o
init|=
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
operator|new
name|AttachmentImpl
argument_list|(
name|key
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|o
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit


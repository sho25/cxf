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
name|attachment
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
name|util
operator|.
name|AbstractCollection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
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
name|Collection
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
name|message
operator|.
name|Attachment
import|;
end_import

begin_class
specifier|public
class|class
name|LazyAttachmentCollection
implements|implements
name|Collection
argument_list|<
name|Attachment
argument_list|>
block|{
specifier|private
name|AttachmentDeserializer
name|deserializer
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|LazyAttachmentCollection
parameter_list|(
name|AttachmentDeserializer
name|deserializer
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|deserializer
operator|=
name|deserializer
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Attachment
argument_list|>
name|getLoadedAttachments
parameter_list|()
block|{
return|return
name|attachments
return|;
block|}
specifier|private
name|void
name|loadAll
parameter_list|()
block|{
try|try
block|{
name|Attachment
name|a
init|=
name|deserializer
operator|.
name|readNext
argument_list|()
decl_stmt|;
while|while
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|attachments
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|a
operator|=
name|deserializer
operator|.
name|readNext
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check for more attachments by attempting to deserialize the next attachment.      *      * @param shouldLoadNew if<i>false</i>, the "loaded attachments" List will not be changed.      * @return there is more attachment or not      * @throws IOException      */
specifier|public
name|boolean
name|hasNext
parameter_list|(
name|boolean
name|shouldLoadNew
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|shouldLoadNew
condition|)
block|{
name|Attachment
name|a
init|=
name|deserializer
operator|.
name|readNext
argument_list|()
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|attachments
operator|.
name|add
argument_list|(
name|a
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
return|return
name|deserializer
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|hasNext
argument_list|(
literal|true
argument_list|)
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
name|Iterator
argument_list|<
name|Attachment
argument_list|>
argument_list|()
block|{
name|int
name|current
decl_stmt|;
name|boolean
name|removed
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
if|if
condition|(
name|attachments
operator|.
name|size
argument_list|()
operator|>
name|current
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// check if there is another attachment
try|try
block|{
name|Attachment
name|a
init|=
name|deserializer
operator|.
name|readNext
argument_list|()
decl_stmt|;
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|attachments
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Attachment
name|next
parameter_list|()
block|{
name|Attachment
name|a
init|=
name|attachments
operator|.
name|get
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|current
operator|++
expr_stmt|;
name|removed
operator|=
literal|false
expr_stmt|;
return|return
name|a
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
if|if
condition|(
name|removed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
name|attachments
operator|.
name|remove
argument_list|(
operator|--
name|current
argument_list|)
expr_stmt|;
name|removed
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
name|loadAll
argument_list|()
expr_stmt|;
return|return
name|attachments
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|add
parameter_list|(
name|Attachment
name|arg0
parameter_list|)
block|{
return|return
name|attachments
operator|.
name|add
argument_list|(
name|arg0
argument_list|)
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
name|arg0
parameter_list|)
block|{
return|return
name|attachments
operator|.
name|addAll
argument_list|(
name|arg0
argument_list|)
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
block|}
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|arg0
parameter_list|)
block|{
return|return
name|attachments
operator|.
name|contains
argument_list|(
name|arg0
argument_list|)
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
name|arg0
parameter_list|)
block|{
return|return
name|attachments
operator|.
name|containsAll
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
if|if
condition|(
name|attachments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|!
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
return|return
name|attachments
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|arg0
parameter_list|)
block|{
return|return
name|attachments
operator|.
name|remove
argument_list|(
name|arg0
argument_list|)
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
name|arg0
parameter_list|)
block|{
return|return
name|attachments
operator|.
name|removeAll
argument_list|(
name|arg0
argument_list|)
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
name|arg0
parameter_list|)
block|{
return|return
name|attachments
operator|.
name|retainAll
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
name|loadAll
argument_list|()
expr_stmt|;
return|return
name|attachments
operator|.
name|toArray
argument_list|()
return|;
block|}
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
name|arg0
parameter_list|)
block|{
name|loadAll
argument_list|()
expr_stmt|;
return|return
name|attachments
operator|.
name|toArray
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|createDataHandlerMap
parameter_list|()
block|{
return|return
operator|new
name|LazyAttachmentMap
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|LazyAttachmentMap
implements|implements
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
block|{
name|LazyAttachmentCollection
name|collection
decl_stmt|;
name|LazyAttachmentMap
parameter_list|(
name|LazyAttachmentCollection
name|c
parameter_list|)
block|{
name|collection
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|it
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Attachment
name|at
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|at
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
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
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|it
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Attachment
name|at
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|equals
argument_list|(
name|at
operator|.
name|getDataHandler
argument_list|()
argument_list|)
condition|)
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
specifier|public
name|DataHandler
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|it
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Attachment
name|at
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|at
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|at
operator|.
name|getDataHandler
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|collection
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|DataHandler
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|it
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Attachment
name|at
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|at
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|collection
operator|.
name|remove
argument_list|(
name|at
argument_list|)
expr_stmt|;
return|return
name|at
operator|.
name|getDataHandler
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|DataHandler
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|DataHandler
name|value
parameter_list|)
block|{
name|Attachment
name|at
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|collection
operator|.
name|add
argument_list|(
name|at
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|DataHandler
argument_list|>
name|t
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|DataHandler
argument_list|>
name|ent
range|:
name|t
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|ent
operator|.
name|getKey
argument_list|()
argument_list|,
name|ent
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Set
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
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|AbstractSet
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
argument_list|()
block|{
specifier|public
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
parameter_list|()
block|{
return|return
operator|new
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
argument_list|()
block|{
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|it
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|next
parameter_list|()
block|{
return|return
operator|new
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
argument_list|()
block|{
name|Attachment
name|at
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|at
operator|.
name|getId
argument_list|()
return|;
block|}
specifier|public
name|DataHandler
name|getValue
parameter_list|()
block|{
return|return
name|at
operator|.
name|getDataHandler
argument_list|()
return|;
block|}
specifier|public
name|DataHandler
name|setValue
parameter_list|(
name|DataHandler
name|value
parameter_list|)
block|{
if|if
condition|(
name|at
operator|instanceof
name|AttachmentImpl
condition|)
block|{
name|DataHandler
name|h
init|=
name|at
operator|.
name|getDataHandler
argument_list|()
decl_stmt|;
operator|(
operator|(
name|AttachmentImpl
operator|)
name|at
operator|)
operator|.
name|setDataHandler
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|h
return|;
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|new
name|AbstractSet
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|it
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|String
name|next
parameter_list|()
block|{
return|return
name|it
operator|.
name|next
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DataHandler
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|new
name|AbstractCollection
argument_list|<
name|DataHandler
argument_list|>
argument_list|()
block|{
specifier|public
name|Iterator
argument_list|<
name|DataHandler
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|DataHandler
argument_list|>
argument_list|()
block|{
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|it
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|DataHandler
name|next
parameter_list|()
block|{
return|return
name|it
operator|.
name|next
argument_list|()
operator|.
name|getDataHandler
argument_list|()
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit


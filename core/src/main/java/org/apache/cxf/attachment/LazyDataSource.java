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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
comment|/**  * A DataSource which will search through a Collection of attachments so as to   * lazily load the attachment from the collection. This allows streaming attachments  * with databinding toolkits like JAXB.  */
end_comment

begin_class
specifier|public
class|class
name|LazyDataSource
implements|implements
name|DataSource
block|{
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|public
name|LazyDataSource
parameter_list|(
name|String
name|id
parameter_list|,
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|load
parameter_list|()
block|{
if|if
condition|(
name|dataSource
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Attachment
name|a
range|:
name|attachments
control|)
block|{
if|if
condition|(
name|a
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getDataSource
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|dataSource
operator|==
literal|null
condition|)
block|{
comment|//couldn't find it, build up error message
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|10
argument_list|)
decl_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|attachments
control|)
block|{
name|ids
operator|.
name|add
argument_list|(
name|a
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|a
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getDataSource
argument_list|()
expr_stmt|;
if|if
condition|(
name|dataSource
operator|!=
literal|null
condition|)
block|{
name|ids
operator|=
literal|null
expr_stmt|;
break|break;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Could not get DataSource for "
operator|+
literal|"attachment of id "
operator|+
name|id
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|ids
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No attachment for "
operator|+
literal|" id "
operator|+
name|id
operator|+
literal|" found in "
operator|+
name|ids
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
name|load
argument_list|()
expr_stmt|;
return|return
name|dataSource
operator|.
name|getContentType
argument_list|()
return|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|load
argument_list|()
expr_stmt|;
return|return
name|dataSource
operator|.
name|getInputStream
argument_list|()
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
name|load
argument_list|()
expr_stmt|;
return|return
name|dataSource
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|load
argument_list|()
expr_stmt|;
return|return
name|dataSource
operator|.
name|getOutputStream
argument_list|()
return|;
block|}
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
name|load
argument_list|()
expr_stmt|;
return|return
name|dataSource
return|;
block|}
specifier|public
name|void
name|setDataSource
parameter_list|(
name|DataSource
name|dataSource
parameter_list|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
block|}
block|}
end_class

end_unit


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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|LoggingMessage
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ID_KEY
init|=
name|LoggingMessage
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ID"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|ID
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|String
name|heading
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|address
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|contentType
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|encoding
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|header
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|message
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|payload
decl_stmt|;
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
specifier|public
name|LoggingMessage
parameter_list|(
name|String
name|h
parameter_list|,
name|String
name|i
parameter_list|)
block|{
name|heading
operator|=
name|h
expr_stmt|;
name|id
operator|=
name|i
expr_stmt|;
name|contentType
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|address
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|encoding
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|header
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|message
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|payload
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|nextId
parameter_list|()
block|{
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|ID
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|StringBuilder
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|StringBuilder
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
specifier|public
name|StringBuilder
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
specifier|public
name|StringBuilder
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
specifier|public
name|StringBuilder
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
specifier|public
name|StringBuilder
name|getPayload
parameter_list|()
block|{
return|return
name|payload
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|heading
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\nID: "
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|address
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\nAddress: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\nEncoding: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\nContent-Type: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\nHeaders: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|header
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\nMessages: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\nPayload: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n--------------------------------------"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


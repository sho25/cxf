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
name|binding
operator|.
name|soap
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
name|apache
operator|.
name|cxf
operator|.
name|headers
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
name|helpers
operator|.
name|CastUtils
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
name|AbstractWrappedMessage
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
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|SoapMessage
extends|extends
name|AbstractWrappedMessage
block|{
specifier|private
name|SoapVersion
name|version
init|=
name|Soap11
operator|.
name|getInstance
argument_list|()
decl_stmt|;
specifier|public
name|SoapMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoapVersion
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|SoapVersion
name|v
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|v
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Header
argument_list|>
name|getHeaders
parameter_list|()
block|{
name|List
argument_list|<
name|Header
argument_list|>
name|heads
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|heads
operator|==
literal|null
condition|)
block|{
name|heads
operator|=
operator|new
name|ArrayList
argument_list|<
name|Header
argument_list|>
argument_list|()
expr_stmt|;
name|put
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|,
name|heads
argument_list|)
expr_stmt|;
block|}
return|return
name|heads
return|;
block|}
specifier|public
name|boolean
name|hasHeader
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
for|for
control|(
name|Header
name|head
range|:
name|getHeaders
argument_list|()
control|)
block|{
if|if
condition|(
name|head
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|qn
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
name|Header
name|getHeader
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
for|for
control|(
name|Header
name|head
range|:
name|getHeaders
argument_list|()
control|)
block|{
if|if
condition|(
name|head
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|qn
argument_list|)
condition|)
block|{
return|return
name|head
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|hasHeaders
parameter_list|()
block|{
return|return
name|containsKey
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|&&
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
block|}
end_class

end_unit


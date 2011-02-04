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
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|client
operator|.
name|service
operator|.
name|settings
package|;
end_package

begin_comment
comment|//TODO this class isn't mine write appropriate comment
end_comment

begin_comment
comment|/**  * Custom Base64 encode/decode implementation suitable for use in GWT applications  * (uses only translatable classes).  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Base64
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ETAB
init|=
literal|"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
decl_stmt|;
specifier|private
name|Base64
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|StringBuffer
name|out
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|r
init|=
name|data
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|r
operator|>
literal|0
condition|)
block|{
name|byte
name|d0
decl_stmt|;
name|byte
name|d1
decl_stmt|;
name|byte
name|d2
decl_stmt|;
name|byte
name|e0
decl_stmt|;
name|byte
name|e1
decl_stmt|;
name|byte
name|e2
decl_stmt|;
name|byte
name|e3
decl_stmt|;
name|d0
operator|=
operator|(
name|byte
operator|)
name|data
operator|.
name|charAt
argument_list|(
name|i
operator|++
argument_list|)
expr_stmt|;
operator|--
name|r
expr_stmt|;
name|e0
operator|=
call|(
name|byte
call|)
argument_list|(
name|d0
operator|>>>
literal|2
argument_list|)
expr_stmt|;
name|e1
operator|=
call|(
name|byte
call|)
argument_list|(
operator|(
name|d0
operator|&
literal|0x03
operator|)
operator|<<
literal|4
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|>
literal|0
condition|)
block|{
name|d1
operator|=
operator|(
name|byte
operator|)
name|data
operator|.
name|charAt
argument_list|(
name|i
operator|++
argument_list|)
expr_stmt|;
operator|--
name|r
expr_stmt|;
name|e1
operator|+=
call|(
name|byte
call|)
argument_list|(
name|d1
operator|>>>
literal|4
argument_list|)
expr_stmt|;
name|e2
operator|=
call|(
name|byte
call|)
argument_list|(
operator|(
name|d1
operator|&
literal|0x0f
operator|)
operator|<<
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|e2
operator|=
literal|64
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|>
literal|0
condition|)
block|{
name|d2
operator|=
operator|(
name|byte
operator|)
name|data
operator|.
name|charAt
argument_list|(
name|i
operator|++
argument_list|)
expr_stmt|;
operator|--
name|r
expr_stmt|;
name|e2
operator|+=
call|(
name|byte
call|)
argument_list|(
name|d2
operator|>>>
literal|6
argument_list|)
expr_stmt|;
name|e3
operator|=
call|(
name|byte
call|)
argument_list|(
name|d2
operator|&
literal|0x3f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|e3
operator|=
literal|64
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
name|ETAB
operator|.
name|charAt
argument_list|(
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|ETAB
operator|.
name|charAt
argument_list|(
name|e1
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|ETAB
operator|.
name|charAt
argument_list|(
name|e2
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|ETAB
operator|.
name|charAt
argument_list|(
name|e3
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


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
name|helpers
package|;
end_package

begin_class
specifier|public
specifier|final
class|class
name|NSDecl
block|{
specifier|private
specifier|final
name|String
name|prefix
decl_stmt|;
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
specifier|private
specifier|final
name|int
name|hashCode
decl_stmt|;
specifier|public
name|NSDecl
parameter_list|(
name|String
name|pfx
parameter_list|,
name|String
name|ur
parameter_list|)
block|{
if|if
condition|(
name|pfx
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|prefix
operator|=
literal|""
operator|.
name|intern
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|prefix
operator|=
name|pfx
operator|.
name|intern
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|uri
operator|=
name|ur
operator|.
name|intern
argument_list|()
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
operator|(
name|toString
argument_list|()
operator|)
operator|.
name|hashCode
argument_list|()
expr_stmt|;
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
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|prefix
operator|+
literal|":"
operator|+
name|uri
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|NSDecl
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|obj
operator|!=
literal|null
operator|&&
name|uri
operator|==
operator|(
operator|(
name|NSDecl
operator|)
name|obj
operator|)
operator|.
name|uri
operator|&&
name|prefix
operator|==
operator|(
operator|(
name|NSDecl
operator|)
name|obj
operator|)
operator|.
name|prefix
return|;
block|}
block|}
end_class

end_unit


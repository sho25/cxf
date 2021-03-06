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
name|systest
operator|.
name|jaxws
operator|.
name|cxf5064
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|HeaderObj
block|{
name|String
name|field1
decl_stmt|;
name|String
name|field2
decl_stmt|;
specifier|public
name|HeaderObj
parameter_list|()
block|{     }
specifier|public
name|HeaderObj
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
index|[]
name|fields
init|=
name|value
operator|.
name|split
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|field1
operator|=
name|fields
index|[
literal|0
index|]
expr_stmt|;
name|field2
operator|=
name|fields
index|[
literal|1
index|]
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getField1
parameter_list|()
block|{
return|return
name|field1
return|;
block|}
specifier|public
name|void
name|setField1
parameter_list|(
name|String
name|field1
parameter_list|)
block|{
name|this
operator|.
name|field1
operator|=
name|field1
expr_stmt|;
block|}
specifier|public
name|String
name|getField2
parameter_list|()
block|{
return|return
name|field2
return|;
block|}
specifier|public
name|void
name|setField2
parameter_list|(
name|String
name|field2
parameter_list|)
block|{
name|this
operator|.
name|field2
operator|=
name|field2
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|field1
operator|+
literal|"-"
operator|+
name|field2
return|;
block|}
block|}
end_class

end_unit


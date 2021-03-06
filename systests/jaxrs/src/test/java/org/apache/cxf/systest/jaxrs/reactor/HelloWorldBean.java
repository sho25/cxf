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
name|jaxrs
operator|.
name|reactor
package|;
end_package

begin_class
specifier|public
class|class
name|HelloWorldBean
block|{
specifier|private
name|String
name|greeting
decl_stmt|;
specifier|private
name|String
name|audience
init|=
literal|"World"
decl_stmt|;
specifier|public
name|HelloWorldBean
parameter_list|()
block|{
name|this
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HelloWorldBean
parameter_list|(
name|String
name|greeting
parameter_list|)
block|{
name|this
operator|.
name|greeting
operator|=
name|greeting
expr_stmt|;
block|}
specifier|public
name|String
name|getGreeting
parameter_list|()
block|{
return|return
name|greeting
return|;
block|}
specifier|public
name|void
name|setGreeting
parameter_list|(
name|String
name|greeting
parameter_list|)
block|{
name|this
operator|.
name|greeting
operator|=
name|greeting
expr_stmt|;
block|}
specifier|public
name|String
name|getAudience
parameter_list|()
block|{
return|return
name|audience
return|;
block|}
specifier|public
name|void
name|setAudience
parameter_list|(
name|String
name|audience
parameter_list|)
block|{
name|this
operator|.
name|audience
operator|=
name|audience
expr_stmt|;
block|}
block|}
end_class

end_unit


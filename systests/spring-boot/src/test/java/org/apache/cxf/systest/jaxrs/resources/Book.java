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
name|resources
package|;
end_package

begin_class
specifier|public
class|class
name|Book
block|{
specifier|private
name|String
name|title
decl_stmt|;
specifier|private
name|String
name|author
decl_stmt|;
specifier|public
name|Book
parameter_list|()
block|{     }
specifier|public
name|Book
parameter_list|(
specifier|final
name|String
name|title
parameter_list|,
specifier|final
name|String
name|author
parameter_list|)
block|{
name|this
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
specifier|public
name|String
name|getAuthor
parameter_list|()
block|{
return|return
name|author
return|;
block|}
specifier|public
name|void
name|setAuthor
parameter_list|(
name|String
name|author
parameter_list|)
block|{
name|this
operator|.
name|author
operator|=
name|author
expr_stmt|;
block|}
block|}
end_class

end_unit


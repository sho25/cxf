begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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

begin_class
specifier|public
class|class
name|BigQueryResponse
block|{
specifier|private
name|String
name|userName
decl_stmt|;
specifier|private
name|String
name|searchWord
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ShakespeareText
argument_list|>
name|texts
init|=
operator|new
name|LinkedList
argument_list|<
name|ShakespeareText
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|BigQueryResponse
parameter_list|()
block|{      }
specifier|public
name|BigQueryResponse
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|searchWord
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|searchWord
operator|=
name|searchWord
expr_stmt|;
block|}
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
specifier|public
name|String
name|getSearchWord
parameter_list|()
block|{
return|return
name|searchWord
return|;
block|}
specifier|public
name|void
name|setSearchWord
parameter_list|(
name|String
name|searchWord
parameter_list|)
block|{
name|this
operator|.
name|searchWord
operator|=
name|searchWord
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ShakespeareText
argument_list|>
name|getTexts
parameter_list|()
block|{
return|return
name|texts
return|;
block|}
specifier|public
name|void
name|setTexts
parameter_list|(
name|List
argument_list|<
name|ShakespeareText
argument_list|>
name|texts
parameter_list|)
block|{
name|this
operator|.
name|texts
operator|=
name|texts
expr_stmt|;
block|}
block|}
end_class

end_unit


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
name|aegis
operator|.
name|type
operator|.
name|array
package|;
end_package

begin_class
specifier|public
class|class
name|FlatArrayService
implements|implements
name|FlatArrayServiceInterface
block|{
name|String
index|[]
name|stringArrayValue
decl_stmt|;
name|BeanWithFlatArray
name|beanWithFlatArrayValue
decl_stmt|;
specifier|public
name|FlatArrayService
parameter_list|()
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|String
index|[]
name|getStringArrayValue
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"bleh"
block|,
literal|"bleh"
block|}
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|submitStringArray
parameter_list|(
name|String
index|[]
name|array
parameter_list|)
block|{
name|stringArrayValue
operator|=
name|array
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|takeBeanWithFlatArray
parameter_list|(
name|BeanWithFlatArray
name|bwfa
parameter_list|)
block|{
name|beanWithFlatArrayValue
operator|=
name|bwfa
expr_stmt|;
block|}
block|}
end_class

end_unit


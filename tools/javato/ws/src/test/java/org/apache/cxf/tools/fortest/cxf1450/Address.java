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
name|tools
operator|.
name|fortest
operator|.
name|cxf1450
package|;
end_package

begin_class
specifier|public
class|class
name|Address
block|{
specifier|private
name|String
name|street
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|city
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|state
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|zipcode
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|country
init|=
literal|""
decl_stmt|;
specifier|public
name|Address
parameter_list|(
name|String
name|street
parameter_list|,
name|String
name|city
parameter_list|,
name|String
name|state
parameter_list|,
name|String
name|zipcode
parameter_list|,
name|String
name|country
parameter_list|)
block|{
name|this
operator|.
name|street
operator|=
name|street
expr_stmt|;
name|this
operator|.
name|city
operator|=
name|city
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
name|this
operator|.
name|zipcode
operator|=
name|zipcode
expr_stmt|;
name|this
operator|.
name|country
operator|=
name|country
expr_stmt|;
block|}
specifier|public
name|Address
parameter_list|()
block|{     }
specifier|public
name|String
name|getStreet
parameter_list|()
block|{
return|return
name|street
return|;
block|}
specifier|public
name|void
name|setStreet
parameter_list|(
name|String
name|street
parameter_list|)
block|{
name|this
operator|.
name|street
operator|=
name|street
expr_stmt|;
block|}
specifier|public
name|String
name|getCity
parameter_list|()
block|{
return|return
name|city
return|;
block|}
specifier|public
name|void
name|setCity
parameter_list|(
name|String
name|city
parameter_list|)
block|{
name|this
operator|.
name|city
operator|=
name|city
expr_stmt|;
block|}
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
specifier|public
name|String
name|getZipcode
parameter_list|()
block|{
return|return
name|zipcode
return|;
block|}
specifier|public
name|void
name|setZipcode
parameter_list|(
name|String
name|zipcode
parameter_list|)
block|{
name|this
operator|.
name|zipcode
operator|=
name|zipcode
expr_stmt|;
block|}
specifier|public
name|String
name|getCountry
parameter_list|()
block|{
return|return
name|country
return|;
block|}
specifier|public
name|void
name|setCountry
parameter_list|(
name|String
name|country
parameter_list|)
block|{
name|this
operator|.
name|country
operator|=
name|country
expr_stmt|;
block|}
block|}
end_class

end_unit


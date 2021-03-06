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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|collections
package|;
end_package

begin_class
specifier|public
class|class
name|CollectionCheckInfo
block|{
specifier|private
name|CollectionCheck
name|checkType
decl_stmt|;
specifier|private
name|Object
name|checkValue
decl_stmt|;
specifier|public
name|CollectionCheckInfo
parameter_list|(
name|CollectionCheck
name|checkType
parameter_list|,
name|Object
name|checkValue
parameter_list|)
block|{
name|this
operator|.
name|checkType
operator|=
name|checkType
expr_stmt|;
name|this
operator|.
name|checkValue
operator|=
name|checkValue
expr_stmt|;
block|}
specifier|public
name|Object
name|getCollectionCheckValue
parameter_list|()
block|{
return|return
name|checkValue
return|;
block|}
specifier|public
name|CollectionCheck
name|getCollectionCheckType
parameter_list|()
block|{
return|return
name|checkType
return|;
block|}
block|}
end_class

end_unit


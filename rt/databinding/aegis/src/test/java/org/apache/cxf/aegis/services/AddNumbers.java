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
name|services
package|;
end_package

begin_class
specifier|public
class|class
name|AddNumbers
block|{
specifier|public
name|int
name|add
parameter_list|(
name|int
name|one
parameter_list|,
name|int
name|two
parameter_list|)
block|{
return|return
name|one
operator|+
name|two
return|;
block|}
specifier|public
name|int
name|unmappedAdd
parameter_list|(
name|int
name|one
parameter_list|,
name|int
name|two
parameter_list|)
block|{
return|return
name|two
operator|+
name|one
return|;
block|}
block|}
end_class

end_unit


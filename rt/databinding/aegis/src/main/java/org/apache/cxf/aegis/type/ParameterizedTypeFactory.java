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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_comment
comment|/**  * Create ParameterizedType objects for some limited cases.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ParameterizedTypeFactory
block|{
specifier|private
name|ParameterizedTypeFactory
parameter_list|()
block|{     }
specifier|public
specifier|static
name|ParameterizedType
name|createParameterizedType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|rawType
parameter_list|,
name|Type
index|[]
name|parameters
parameter_list|)
block|{
return|return
operator|new
name|SimpleParameterizedType
argument_list|(
name|rawType
argument_list|,
name|parameters
argument_list|)
return|;
block|}
block|}
end_class

end_unit


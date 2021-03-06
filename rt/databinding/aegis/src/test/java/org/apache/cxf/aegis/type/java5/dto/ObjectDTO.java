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
name|java5
operator|.
name|dto
package|;
end_package

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
name|ObjectDTO
block|{
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|objects
decl_stmt|;
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|getObjects
parameter_list|()
block|{
return|return
name|objects
return|;
block|}
specifier|public
name|void
name|setObjects
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|objects
parameter_list|)
block|{
name|this
operator|.
name|objects
operator|=
name|objects
expr_stmt|;
block|}
block|}
end_class

end_unit


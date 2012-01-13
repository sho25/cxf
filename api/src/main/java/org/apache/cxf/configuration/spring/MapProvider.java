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
name|configuration
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This is to workaround an issue with Spring.  *   * In spring, if you inject a Map<X, V> into a contructor, it   * ALWAYS will call entrySet and copy the entries into a new  * map (HashMap).    Thus, any "deferred" processing will happen  * immediately.  Also, things like the Bus may not be completely  * initialized.    *   * We'll mark some of our Spring things with this interface and   * allow the MapProvider to be injected.     */
end_comment

begin_interface
specifier|public
interface|interface
name|MapProvider
parameter_list|<
name|X
parameter_list|,
name|V
parameter_list|>
block|{
name|Map
argument_list|<
name|X
argument_list|,
name|V
argument_list|>
name|createMap
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


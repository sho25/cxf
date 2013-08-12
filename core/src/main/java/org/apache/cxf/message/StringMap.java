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
name|message
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

begin_interface
specifier|public
interface|interface
name|StringMap
extends|extends
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
block|{
comment|/**      * Convenience method for storing/retrieving typed objects from the map.      * equivalent to:  (T)get(key.getName());      * @param key the key      * @return the value      */
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
function_decl|;
comment|/**      * Convenience method for storing/retrieving typed objects from the map.      * equivalent to:  put(key.getName(), value);      * @param key the key      * @param value the value      */
parameter_list|<
name|T
parameter_list|>
name|void
name|put
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|T
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


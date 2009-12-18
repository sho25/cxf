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
name|atom
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * A callback-style interface which can be used to map an object to an Atom Feed or Entry  * without having to introduce direct dependencies on Abdera API in the 'main' service code    *   * @param<T> Type of Atom element, Feed or Entry  * @param<E> Type of objects which will be mapped to feed or entry  *    */
end_comment

begin_interface
specifier|public
interface|interface
name|AtomElementWriter
parameter_list|<
name|T
extends|extends
name|Element
parameter_list|,
name|E
parameter_list|>
block|{
comment|/**      * @param element Feed or Entry instance       * @param pojoElement An object which needs to be mapped to the feed or entry      */
name|void
name|writeTo
parameter_list|(
name|T
name|element
parameter_list|,
name|E
name|pojoElement
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


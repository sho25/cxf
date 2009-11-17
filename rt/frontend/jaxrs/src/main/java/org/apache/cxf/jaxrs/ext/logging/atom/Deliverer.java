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
name|logging
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
comment|/**  * ATOM element deliverer. Represents transport strategy e.g. using  * {@link org.apache.cxf.jaxrs.client.WebClient}, SOAP reliable messaging etc.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Deliverer
block|{
comment|/**      * Delivers ATOM element.      *       * @param element element to deliver.      * @return true if delivery successful, false otherwise.      */
name|boolean
name|deliver
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|InterruptedException
function_decl|;
block|}
end_interface

end_unit


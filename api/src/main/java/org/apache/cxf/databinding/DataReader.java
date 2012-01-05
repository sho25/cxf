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
name|databinding
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_comment
comment|/**  * The 'read' side of the data binding abstraction of CXF. A DataReader&lt;T&gt; reads objects   * from a source of type T.  * @param<T> The type of the source. Each data binding defines the set of source types that it supports.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataReader
parameter_list|<
name|T
parameter_list|>
extends|extends
name|BaseDataReader
block|{
comment|/**      * Read an object from the input.      * @param input input source object.      * @return item read.      */
name|Object
name|read
parameter_list|(
name|T
name|input
parameter_list|)
function_decl|;
comment|/**      * Read an object from the input, applying additional conventions based on the WSDL message      * part.      * @param part The message part for this item. If null, this API is equivalent to      * {@link #read(Object)}.      * @param input input source object.      * @return item read.      */
name|Object
name|read
parameter_list|(
name|MessagePartInfo
name|part
parameter_list|,
name|T
name|input
parameter_list|)
function_decl|;
comment|/**      * Read an object from the input. In the current version of CXF, not all binding support      * this API, and those that do ignore the element QName parameter.      * @param elementQName expected element. Generally ignored.      * @param input input source object.      * @param type the type of object required/requested. This is generally used       * when the caller wants to receive a raw source object and avoid any binding processing.      * For example, passing javax.xml.transform.Source. The bindings do not necessarily throw      * if they cannot provide an object of the requested type, and will apply their normal      * mapping processing, instead.      * @return item read.      */
name|Object
name|read
parameter_list|(
name|QName
name|elementQName
parameter_list|,
name|T
name|input
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit


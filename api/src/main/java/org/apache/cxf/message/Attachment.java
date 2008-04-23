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
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Attachment
block|{
name|DataHandler
name|getDataHandler
parameter_list|()
function_decl|;
comment|/**      * @return The attachment id.      */
name|String
name|getId
parameter_list|()
function_decl|;
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|getHeaderNames
parameter_list|()
function_decl|;
comment|/**      * Whether or not this is an XOP package. This will affect the       * serialization of the attachment. If true, it will be serialized      * as binary data, and not Base64Binary.      *       * @return true iff this attachment is an XOP package      */
name|boolean
name|isXOP
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


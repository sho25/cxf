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
name|binding
operator|.
name|corba
operator|.
name|types
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|Namespace
import|;
end_import

begin_interface
specifier|public
interface|interface
name|CorbaTypeEventProducer
block|{
name|boolean
name|hasNext
parameter_list|()
function_decl|;
comment|/*       * return the current event      */
name|int
name|next
parameter_list|()
function_decl|;
comment|/*      * qname of current content      */
name|QName
name|getName
parameter_list|()
function_decl|;
comment|/*      * local name of current content      */
name|String
name|getLocalName
parameter_list|()
function_decl|;
comment|/*      * text of current content      */
name|String
name|getText
parameter_list|()
function_decl|;
comment|/*      * return any attributes for the current type      */
name|List
argument_list|<
name|Attribute
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
comment|/*      * return any namespace for the current type      */
name|List
argument_list|<
name|Namespace
argument_list|>
name|getNamespaces
parameter_list|()
function_decl|;
block|}
end_interface

end_unit


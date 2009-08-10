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
name|annotations
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Adds documentation nodes to the generated WSDL  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
specifier|public
annotation_defn|@interface
name|WSDLDocumentation
block|{
comment|/**      * The documentation to add      * @return documentation string      */
name|String
name|value
parameter_list|()
function_decl|;
comment|/**      * The place to put the documentation.  The Default is depends on the       * location of the annotation.   On the method in the SEI, it would be      * the portType/operation, on the SEI, it would be the portType, on the       * service impl, the service element.      * @return location      */
name|Placement
name|placement
parameter_list|()
default|default
name|Placement
operator|.
name|DEFAULT
function_decl|;
comment|/**      * If Placement is FAULT_MESSAGE, PORT_FAULT, or BINDING_FAULT,      * return the fault class associated with this documentation       * @return the fault class      */
name|Class
argument_list|<
name|?
argument_list|>
name|faultClass
parameter_list|()
default|default
name|DEFAULT
operator|.
name|class
function_decl|;
enum|enum
name|Placement
block|{
name|DEFAULT
block|,
name|TOP
block|,
name|INPUT_MESSAGE
block|,
name|OUTPUT_MESSAGE
block|,
name|FAULT_MESSAGE
block|,
name|PORT_TYPE
block|,
name|PORT_TYPE_OPERATION
block|,
name|PORT_TYPE_OPERATION_INPUT
block|,
name|PORT_TYPE_OPERATION_OUTPUT
block|,
name|PORT_TYPE_OPERATION_FAULT
block|,
name|BINDING
block|,
name|BINDING_OPERATION
block|,
name|BINDING_OPERATION_INPUT
block|,
name|BINDING_OPERATION_OUTPUT
block|,
name|BINDING_OPERATION_FAULT
block|,
name|SERVICE
block|,
name|SERVICE_PORT
block|,              }
empty_stmt|;
specifier|static
specifier|final
class|class
name|DEFAULT
block|{ }
block|}
end_annotation_defn

end_unit


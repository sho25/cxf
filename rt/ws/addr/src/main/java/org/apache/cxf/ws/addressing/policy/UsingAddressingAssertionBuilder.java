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
name|ws
operator|.
name|addressing
operator|.
name|policy
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
name|ws
operator|.
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertionBuilder
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|UsingAddressingAssertionBuilder
extends|extends
name|PrimitiveAssertionBuilder
block|{
specifier|private
specifier|static
specifier|final
name|QName
index|[]
name|KNOWN_ELEMENTS
init|=
block|{
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2004_QNAME
block|,
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2005_QNAME
block|,
name|MetadataConstants
operator|.
name|USING_ADDRESSING_2006_QNAME
block|,     }
decl_stmt|;
specifier|public
name|UsingAddressingAssertionBuilder
parameter_list|()
block|{
name|super
argument_list|(
name|KNOWN_ELEMENTS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


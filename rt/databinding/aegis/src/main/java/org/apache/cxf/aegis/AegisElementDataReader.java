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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
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
name|staxutils
operator|.
name|W3CDOMStreamReader
import|;
end_import

begin_class
specifier|public
class|class
name|AegisElementDataReader
extends|extends
name|AbstractAegisIoImpl
implements|implements
name|AegisReader
argument_list|<
name|Element
argument_list|>
block|{
specifier|protected
name|AegisXMLStreamDataReader
name|reader
decl_stmt|;
specifier|public
name|AegisElementDataReader
parameter_list|(
name|AegisContext
name|globalContext
parameter_list|)
block|{
name|super
argument_list|(
name|globalContext
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|AegisXMLStreamDataReader
argument_list|(
name|globalContext
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert a DOM element to a type.      * @param input      * @return      */
specifier|public
name|Object
name|read
parameter_list|(
name|Element
name|input
parameter_list|)
throws|throws
name|Exception
block|{
name|W3CDOMStreamReader
name|sreader
init|=
operator|new
name|W3CDOMStreamReader
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|sreader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|//advance into the first tag
return|return
name|reader
operator|.
name|read
argument_list|(
name|sreader
argument_list|)
return|;
block|}
specifier|public
name|Object
name|read
parameter_list|(
name|Element
name|input
parameter_list|,
name|AegisType
name|desiredType
parameter_list|)
throws|throws
name|Exception
block|{
name|W3CDOMStreamReader
name|sreader
init|=
operator|new
name|W3CDOMStreamReader
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|sreader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|//advance into the first tag
return|return
name|reader
operator|.
name|read
argument_list|(
name|sreader
argument_list|,
name|desiredType
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
operator|.
name|type
operator|.
name|basic
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
name|aegis
operator|.
name|Context
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
name|DatabindingException
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
name|Type
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
name|xml
operator|.
name|MessageReader
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
name|xml
operator|.
name|MessageWriter
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|CharacterAsStringType
extends|extends
name|Type
block|{
specifier|public
specifier|static
specifier|final
name|QName
name|CHARACTER_AS_STRING_TYPE_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/aegisTypes"
argument_list|,
literal|"char"
argument_list|)
decl_stmt|;
specifier|private
name|IntType
name|intType
decl_stmt|;
specifier|public
name|CharacterAsStringType
parameter_list|()
block|{
name|intType
operator|=
operator|new
name|IntType
argument_list|()
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|Integer
name|readInteger
init|=
operator|(
name|Integer
operator|)
name|intType
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
return|return
operator|new
name|Character
argument_list|(
operator|(
name|char
operator|)
name|readInteger
operator|.
name|intValue
argument_list|()
argument_list|)
return|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|Character
name|charObject
init|=
operator|(
name|Character
operator|)
name|object
decl_stmt|;
name|intType
operator|.
name|writeObject
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|charObject
operator|.
name|charValue
argument_list|()
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|usesUtilityTypes
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit


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
name|tools
operator|.
name|corba
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|wsdl
operator|.
name|CorbaConstants
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|CorbaType
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaPrimitiveMap
extends|extends
name|PrimitiveMapBase
block|{
specifier|public
name|CorbaPrimitiveMap
parameter_list|()
block|{
name|corbaPrimitiveMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|initialiseMap
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initialiseMap
parameter_list|()
block|{
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"string"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_BOOLEAN
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_FLOAT
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_DOUBLE
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"dateTime"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_DATETIME
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"time"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"gYearMonth"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"gYear"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"gMonthDay"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"gMonth"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"gDay"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"duration"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"anyURI"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"QName"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"normalizedString"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"token"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"language"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"NMTOKEN"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"Name"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"NCName"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"ID"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_STRING
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"integer"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_LONGLONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_SHORT
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_CHAR
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_LONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_LONGLONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"nonPositiveInteger"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_LONGLONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"negativeInteger"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_LONGLONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"nonNegativeInteger"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_ULONGLONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"positiveInteger"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_ULONGLONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"unsignedInt"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_ULONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"unsignedLong"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_ULONGLONG
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"unsignedShort"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_USHORT
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"unsignedByte"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_OCTET
argument_list|)
expr_stmt|;
name|corbaPrimitiveMap
operator|.
name|put
argument_list|(
literal|"anyType"
argument_list|,
name|CorbaConstants
operator|.
name|NT_CORBA_ANY
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|QName
name|key
parameter_list|)
block|{
name|CorbaType
name|corbaTypeImpl
init|=
literal|null
decl_stmt|;
name|QName
name|type
init|=
name|corbaPrimitiveMap
operator|.
name|get
argument_list|(
name|key
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|corbaTypeImpl
operator|=
operator|new
name|CorbaType
argument_list|()
expr_stmt|;
name|corbaTypeImpl
operator|.
name|setQName
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|corbaTypeImpl
operator|.
name|setType
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|corbaTypeImpl
operator|.
name|setName
argument_list|(
name|key
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|corbaTypeImpl
return|;
block|}
block|}
end_class

end_unit


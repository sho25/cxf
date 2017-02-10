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
name|util
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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|BuiltInTypesJavaMappingUtil
block|{
specifier|private
specifier|static
specifier|final
name|String
name|XML_SCHEMA_NS
init|=
literal|"http://www.w3.org/2000/10/XMLSchema"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NS_XMLNS
init|=
literal|"http://www.w3.org/2000/xmlns/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NS_XSD
init|=
literal|"http://www.w3.org/2001/XMLSchema"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NS_XSI
init|=
literal|"http://www.w3.org/2001/XMLSchema-instance"
decl_stmt|;
specifier|private
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|nameSpaces
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|nameSpaces
operator|.
name|add
argument_list|(
name|XML_SCHEMA_NS
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|nameSpaces
operator|.
name|add
argument_list|(
name|NS_XMLNS
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|nameSpaces
operator|.
name|add
argument_list|(
name|NS_XSD
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|nameSpaces
operator|.
name|add
argument_list|(
name|NS_XSI
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jTypeMapping
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"string"
argument_list|,
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"integer"
argument_list|,
literal|"java.math.BigInteger"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
literal|"int"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
literal|"long"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
literal|"short"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"decimal"
argument_list|,
literal|"java.math.BigDecimal"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
literal|"float"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
literal|"double"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
literal|"boolean"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
literal|"byte"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"qname"
argument_list|,
literal|"javax.xml.namespace.QName"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"dataTime"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"time"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"dataTime"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"gday"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"gmonth"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"gyear"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"gmonthday"
argument_list|,
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"base64binary"
argument_list|,
literal|"byte[]"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"hexbinary"
argument_list|,
literal|"byte[]"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"unsignedint"
argument_list|,
literal|"long"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"unsignedshort"
argument_list|,
literal|"int"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"unsignedbyte"
argument_list|,
literal|"short"
argument_list|)
expr_stmt|;
name|jTypeMapping
operator|.
name|put
argument_list|(
literal|"anytype"
argument_list|,
literal|"Object"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BuiltInTypesJavaMappingUtil
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|getJType
parameter_list|(
name|String
name|nameSpace
parameter_list|,
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|nameSpace
operator|==
literal|null
operator|||
operator|!
name|nameSpaces
operator|.
name|contains
argument_list|(
name|nameSpace
operator|.
name|toLowerCase
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|jTypeMapping
operator|.
name|get
argument_list|(
name|type
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit


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
name|common
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

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
name|Map
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

begin_class
specifier|public
class|class
name|JavaType
block|{
specifier|public
enum|enum
name|Style
block|{
name|IN
block|,
name|OUT
block|,
name|INOUT
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeMapping
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
name|typeMapping
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
literal|"Short.parseShort(\"0\")"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
literal|"Byte.parseByte(\"0\")"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
literal|"0.0f"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
literal|"0.0"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"char"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"java.lang.String"
argument_list|,
literal|"\"\""
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"javax.xml.namespace.QName"
argument_list|,
literal|"new javax.xml.namespace.QName(\"\", \"\")"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"java.net.URI"
argument_list|,
literal|"new java.net.URI(\"\")"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"java.math.BigInteger"
argument_list|,
literal|"new java.math.BigInteger(\"0\")"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"java.math.BigDecimal"
argument_list|,
literal|"new java.math.BigDecimal(\"0\")"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"javax.xml.datatype.XMLGregorianCalendar"
argument_list|,
literal|"null"
argument_list|)
expr_stmt|;
name|typeMapping
operator|.
name|put
argument_list|(
literal|"javax.xml.datatype.Duration"
argument_list|,
literal|"null"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|type
decl_stmt|;
specifier|protected
name|String
name|packageName
decl_stmt|;
specifier|protected
name|String
name|className
decl_stmt|;
specifier|protected
name|String
name|simpleName
decl_stmt|;
specifier|protected
name|String
name|targetNamespace
decl_stmt|;
specifier|protected
name|Style
name|style
decl_stmt|;
specifier|protected
name|boolean
name|isHeader
decl_stmt|;
specifier|private
name|QName
name|qname
decl_stmt|;
specifier|private
name|JavaInterface
name|owner
decl_stmt|;
specifier|private
name|DefaultValueWriter
name|dvw
decl_stmt|;
specifier|public
name|JavaType
parameter_list|()
block|{     }
specifier|public
name|JavaType
parameter_list|(
name|String
name|n
parameter_list|,
name|String
name|t
parameter_list|,
name|String
name|tns
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|n
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|t
expr_stmt|;
name|this
operator|.
name|targetNamespace
operator|=
name|tns
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|t
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultValueWriter
parameter_list|(
name|DefaultValueWriter
name|w
parameter_list|)
block|{
name|dvw
operator|=
name|w
expr_stmt|;
block|}
specifier|public
name|DefaultValueWriter
name|getDefaultValueWriter
parameter_list|()
block|{
return|return
name|dvw
return|;
block|}
specifier|public
name|void
name|setQName
parameter_list|(
name|QName
name|qn
parameter_list|)
block|{
name|this
operator|.
name|qname
operator|=
name|qn
expr_stmt|;
block|}
specifier|public
name|QName
name|getQName
parameter_list|()
block|{
return|return
name|this
operator|.
name|qname
return|;
block|}
specifier|public
name|void
name|setClassName
parameter_list|(
name|String
name|clzName
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|clzName
expr_stmt|;
name|resolvePackage
argument_list|(
name|clzName
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|resolvePackage
parameter_list|(
name|String
name|clzName
parameter_list|)
block|{
if|if
condition|(
name|clzName
operator|==
literal|null
operator|||
name|clzName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|this
operator|.
name|packageName
operator|=
literal|""
expr_stmt|;
name|this
operator|.
name|simpleName
operator|=
name|clzName
expr_stmt|;
block|}
else|else
block|{
name|int
name|index
init|=
name|clzName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|this
operator|.
name|packageName
operator|=
name|clzName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|this
operator|.
name|simpleName
operator|=
name|clzName
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|this
operator|.
name|className
return|;
block|}
specifier|public
name|void
name|writeDefaultValue
parameter_list|(
name|Writer
name|writer
parameter_list|,
name|String
name|indent
parameter_list|,
name|String
name|opName
parameter_list|,
name|String
name|varName
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|dvw
operator|!=
literal|null
condition|)
block|{
name|dvw
operator|.
name|writeDefaultValue
argument_list|(
name|writer
argument_list|,
name|indent
argument_list|,
name|opName
argument_list|,
name|varName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|write
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|varName
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|" = "
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|getDefaultTypeValue
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|className
operator|.
name|trim
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|)
block|{
return|return
literal|"new "
operator|+
name|this
operator|.
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|this
operator|.
name|className
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
operator|+
literal|"[0]"
return|;
block|}
if|if
condition|(
name|typeMapping
operator|.
name|containsKey
argument_list|(
name|this
operator|.
name|className
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|typeMapping
operator|.
name|get
argument_list|(
name|this
operator|.
name|className
argument_list|)
return|;
block|}
try|try
block|{
if|if
condition|(
name|hasDefaultConstructor
argument_list|(
name|Class
operator|.
name|forName
argument_list|(
name|this
operator|.
name|className
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|"new "
operator|+
name|this
operator|.
name|className
operator|+
literal|"()"
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// DONE
block|}
return|return
literal|"null"
return|;
block|}
specifier|private
name|boolean
name|hasDefaultConstructor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
index|[]
name|cons
init|=
name|clz
operator|.
name|getConstructors
argument_list|()
decl_stmt|;
if|if
condition|(
name|cons
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cons
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|cons
index|[
name|i
index|]
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setTargetNamespace
parameter_list|(
name|String
name|tns
parameter_list|)
block|{
name|this
operator|.
name|targetNamespace
operator|=
name|tns
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetNamespace
parameter_list|()
block|{
return|return
name|this
operator|.
name|targetNamespace
return|;
block|}
specifier|public
name|String
name|getRawName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|name
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|t
parameter_list|)
block|{
name|type
operator|=
name|t
expr_stmt|;
block|}
comment|//
comment|// getter and setter for in, out inout style
comment|//
specifier|public
name|JavaType
operator|.
name|Style
name|getStyle
parameter_list|()
block|{
return|return
name|this
operator|.
name|style
return|;
block|}
specifier|public
name|void
name|setStyle
parameter_list|(
name|Style
name|s
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIN
parameter_list|()
block|{
return|return
name|this
operator|.
name|style
operator|==
name|Style
operator|.
name|IN
return|;
block|}
specifier|public
name|boolean
name|isOUT
parameter_list|()
block|{
return|return
name|this
operator|.
name|style
operator|==
name|Style
operator|.
name|OUT
return|;
block|}
specifier|public
name|boolean
name|isINOUT
parameter_list|()
block|{
return|return
name|this
operator|.
name|style
operator|==
name|Style
operator|.
name|INOUT
return|;
block|}
specifier|public
name|void
name|setHeader
parameter_list|(
name|boolean
name|header
parameter_list|)
block|{
name|this
operator|.
name|isHeader
operator|=
name|header
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHeader
parameter_list|()
block|{
return|return
name|this
operator|.
name|isHeader
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\nName: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|this
operator|.
name|name
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\nType: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|this
operator|.
name|type
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\nClassName: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|this
operator|.
name|className
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\nTargetNamespace: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|this
operator|.
name|targetNamespace
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\nStyle: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|JavaInterface
name|getOwner
parameter_list|()
block|{
return|return
name|this
operator|.
name|owner
return|;
block|}
specifier|public
name|void
name|setOwner
parameter_list|(
name|JavaInterface
name|intf
parameter_list|)
block|{
name|this
operator|.
name|owner
operator|=
name|intf
expr_stmt|;
block|}
specifier|public
name|String
name|getPackageName
parameter_list|()
block|{
return|return
name|this
operator|.
name|packageName
return|;
block|}
specifier|public
name|String
name|getSimpleName
parameter_list|()
block|{
return|return
name|this
operator|.
name|simpleName
return|;
block|}
block|}
end_class

end_unit


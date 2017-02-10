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
name|BufferedReader
import|;
end_import

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
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
import|;
end_import

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
name|tools
operator|.
name|common
operator|.
name|ToolException
import|;
end_import

begin_class
specifier|public
class|class
name|JavaInterface
implements|implements
name|JavaAnnotatable
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|packageName
decl_stmt|;
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|String
name|location
decl_stmt|;
specifier|private
name|String
name|packageJavaDoc
decl_stmt|;
specifier|private
name|String
name|classJavaDoc
decl_stmt|;
specifier|private
name|JavaModel
name|model
decl_stmt|;
specifier|private
name|SOAPBinding
operator|.
name|Style
name|soapStyle
decl_stmt|;
specifier|private
name|SOAPBinding
operator|.
name|Use
name|soapUse
decl_stmt|;
specifier|private
name|SOAPBinding
operator|.
name|ParameterStyle
name|soapParameterStyle
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|JAnnotation
argument_list|>
name|annotations
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|imports
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|supers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|webserviceName
decl_stmt|;
specifier|private
name|Element
name|handlerChains
decl_stmt|;
specifier|public
name|JavaInterface
parameter_list|()
block|{     }
specifier|public
name|JavaInterface
parameter_list|(
name|JavaModel
name|m
parameter_list|)
block|{
name|this
operator|.
name|model
operator|=
name|m
expr_stmt|;
block|}
specifier|static
name|String
name|formatJavaDoc
parameter_list|(
name|String
name|d
parameter_list|,
name|String
name|spaces
parameter_list|)
block|{
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|d2
init|=
operator|new
name|StringBuilder
argument_list|(
name|d
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|StringReader
name|r
init|=
operator|new
name|StringReader
argument_list|(
name|d
argument_list|)
decl_stmt|;
name|BufferedReader
name|r2
init|=
operator|new
name|BufferedReader
argument_list|(
name|r
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|s2
init|=
name|r2
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|pfx
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|s2
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|pfx
operator|==
literal|null
operator|&&
name|s2
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|pfx
operator|=
literal|""
expr_stmt|;
while|while
condition|(
name|s2
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|s2
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|pfx
operator|+=
literal|" "
expr_stmt|;
name|s2
operator|=
name|s2
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|pfx
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|d2
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|d2
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|d2
operator|.
name|append
argument_list|(
name|spaces
argument_list|)
operator|.
name|append
argument_list|(
literal|"* "
argument_list|)
expr_stmt|;
if|if
condition|(
name|s2
operator|.
name|startsWith
argument_list|(
name|pfx
argument_list|)
condition|)
block|{
name|d2
operator|.
name|append
argument_list|(
name|s2
operator|.
name|substring
argument_list|(
name|pfx
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|d2
operator|.
name|append
argument_list|(
name|s2
argument_list|)
expr_stmt|;
block|}
block|}
name|s2
operator|=
name|r2
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|d
operator|=
name|d2
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|//ignore, use the raw value
block|}
block|}
return|return
name|d
return|;
block|}
specifier|public
name|void
name|setWebServiceName
parameter_list|(
name|String
name|wsn
parameter_list|)
block|{
name|this
operator|.
name|webserviceName
operator|=
name|wsn
expr_stmt|;
block|}
specifier|public
name|String
name|getWebServiceName
parameter_list|()
block|{
return|return
name|this
operator|.
name|webserviceName
return|;
block|}
specifier|public
name|void
name|setSOAPStyle
parameter_list|(
name|SOAPBinding
operator|.
name|Style
name|s
parameter_list|)
block|{
name|this
operator|.
name|soapStyle
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|SOAPBinding
operator|.
name|Style
name|getSOAPStyle
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapStyle
return|;
block|}
specifier|public
name|void
name|setSOAPUse
parameter_list|(
name|SOAPBinding
operator|.
name|Use
name|u
parameter_list|)
block|{
name|this
operator|.
name|soapUse
operator|=
name|u
expr_stmt|;
block|}
specifier|public
name|SOAPBinding
operator|.
name|Use
name|getSOAPUse
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapUse
return|;
block|}
specifier|public
name|void
name|setSOAPParameterStyle
parameter_list|(
name|SOAPBinding
operator|.
name|ParameterStyle
name|p
parameter_list|)
block|{
name|this
operator|.
name|soapParameterStyle
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|SOAPBinding
operator|.
name|ParameterStyle
name|getSOAPParameterStyle
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapParameterStyle
return|;
block|}
specifier|public
name|JavaModel
name|getJavaModel
parameter_list|()
block|{
return|return
name|this
operator|.
name|model
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|l
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|l
expr_stmt|;
block|}
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|this
operator|.
name|location
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSuperInterfaces
parameter_list|()
block|{
return|return
name|supers
return|;
block|}
specifier|public
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|getMethods
parameter_list|()
block|{
return|return
name|methods
return|;
block|}
specifier|public
name|boolean
name|hasMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
block|{
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|String
name|signature
init|=
name|method
operator|.
name|getSignature
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|methods
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|signature
operator|.
name|equals
argument_list|(
name|methods
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getSignature
argument_list|()
argument_list|)
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
name|int
name|indexOf
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
block|{
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|String
name|signature
init|=
name|method
operator|.
name|getSignature
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|methods
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|signature
operator|.
name|equals
argument_list|(
name|methods
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getSignature
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|int
name|removeMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
block|{
name|int
name|index
init|=
name|indexOf
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
operator|-
literal|1
condition|)
block|{
name|methods
operator|.
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
return|return
name|index
return|;
block|}
specifier|public
name|void
name|replaceMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
block|{
name|int
name|index
init|=
name|removeMethod
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|methods
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addMethod
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
throws|throws
name|ToolException
block|{
if|if
condition|(
name|hasMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|replaceMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|methods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addSuperInterface
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|s
operator|.
name|startsWith
argument_list|(
literal|"java.lang."
argument_list|)
condition|)
block|{
name|addImport
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
name|s
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|supers
operator|.
name|add
argument_list|(
name|s
argument_list|)
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
name|void
name|setPackageName
parameter_list|(
name|String
name|pn
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|pn
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|this
operator|.
name|namespace
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|ns
expr_stmt|;
block|}
specifier|public
name|void
name|setPackageJavaDoc
parameter_list|(
name|String
name|doc
parameter_list|)
block|{
name|packageJavaDoc
operator|=
name|formatJavaDoc
argument_list|(
name|doc
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPackageJavaDoc
parameter_list|()
block|{
return|return
operator|(
name|packageJavaDoc
operator|!=
literal|null
operator|)
condition|?
name|packageJavaDoc
else|:
literal|""
return|;
block|}
specifier|public
name|void
name|setClassJavaDoc
parameter_list|(
name|String
name|doc
parameter_list|)
block|{
name|classJavaDoc
operator|=
name|formatJavaDoc
argument_list|(
name|doc
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getClassJavaDoc
parameter_list|()
block|{
return|return
operator|(
name|classJavaDoc
operator|!=
literal|null
operator|)
condition|?
name|classJavaDoc
else|:
literal|""
return|;
block|}
specifier|public
name|void
name|addAnnotation
parameter_list|(
name|JAnnotation
name|annotation
parameter_list|)
block|{
name|this
operator|.
name|annotations
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|importClz
range|:
name|annotation
operator|.
name|getImports
argument_list|()
control|)
block|{
name|addImport
argument_list|(
name|importClz
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|JAnnotation
argument_list|>
name|getAnnotations
parameter_list|()
block|{
return|return
name|this
operator|.
name|annotations
return|;
block|}
specifier|public
name|void
name|addImport
parameter_list|(
name|String
name|i
parameter_list|)
block|{
if|if
condition|(
name|i
operator|!=
literal|null
operator|&&
name|i
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
name|getPackageName
argument_list|()
operator|!=
literal|null
operator|&&
name|getPackageName
argument_list|()
operator|.
name|equals
argument_list|(
name|i
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// replace "$" with "." to correctly deal with member classes
if|if
condition|(
name|i
operator|!=
literal|null
condition|)
block|{
name|imports
operator|.
name|add
argument_list|(
name|i
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"\\."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addImports
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|ii
parameter_list|)
block|{
for|for
control|(
name|String
name|i
range|:
name|ii
control|)
block|{
comment|// replace "$" with "." to correctly deal with member classes
name|imports
operator|.
name|add
argument_list|(
name|i
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"\\."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getImports
parameter_list|()
block|{
return|return
name|imports
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|void
name|setJavaModel
parameter_list|(
name|JavaModel
name|jm
parameter_list|)
block|{
name|this
operator|.
name|model
operator|=
name|jm
expr_stmt|;
block|}
specifier|public
name|void
name|annotate
parameter_list|(
name|Annotator
name|annotator
parameter_list|)
block|{
name|annotator
operator|.
name|annotate
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Element
name|getHandlerChains
parameter_list|()
block|{
return|return
name|this
operator|.
name|handlerChains
return|;
block|}
specifier|public
name|void
name|setHandlerChains
parameter_list|(
name|Element
name|elem
parameter_list|)
block|{
name|this
operator|.
name|handlerChains
operator|=
name|elem
expr_stmt|;
block|}
specifier|public
name|void
name|setFullClassName
parameter_list|(
name|String
name|fullName
parameter_list|)
block|{
name|int
name|index
init|=
name|fullName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|setPackageName
argument_list|(
name|fullName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|fullName
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|fullName
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getFullClassName
parameter_list|()
block|{
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
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getName
argument_list|()
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
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|JAnnotation
name|anno
range|:
name|annotations
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|anno
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|getFullClassName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit


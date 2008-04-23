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
name|systest
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Vector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
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
name|feature
operator|.
name|Features
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
name|message
operator|.
name|Exchange
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
name|systest
operator|.
name|jaxws
operator|.
name|types
operator|.
name|Bar
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
name|systest
operator|.
name|jaxws
operator|.
name|types
operator|.
name|BarImpl
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.systest.jaxws.DocLitWrappedCodeFirstService"
argument_list|,
name|serviceName
operator|=
literal|"DocLitWrappedCodeFirstService"
argument_list|,
name|portName
operator|=
literal|"DocLitWrappedCodeFirstServicePort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws/DocLitWrappedCodeFirstService"
argument_list|)
annotation|@
name|Features
argument_list|(
name|features
operator|=
block|{
literal|"org.apache.cxf.feature.FastInfosetFeature"
block|}
argument_list|)
specifier|public
class|class
name|DocLitWrappedCodeFirstServiceImpl
implements|implements
name|DocLitWrappedCodeFirstService
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DATA
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"string1"
block|,
literal|"string2"
block|,
literal|"string3"
block|}
decl_stmt|;
annotation|@
name|Resource
name|WebServiceContext
name|context
decl_stmt|;
specifier|public
name|int
name|thisShouldNotBeInTheWSDL
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|i
return|;
block|}
specifier|public
name|String
index|[]
name|arrayOutput
parameter_list|()
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No CONTEXT!!!"
argument_list|)
throw|;
block|}
return|return
name|DATA
return|;
block|}
specifier|public
name|Vector
argument_list|<
name|String
argument_list|>
name|listOutput
parameter_list|()
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No CONTEXT!!!"
argument_list|)
throw|;
block|}
return|return
operator|new
name|Vector
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|DATA
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|arrayInput
parameter_list|(
name|String
index|[]
name|inputs
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No CONTEXT!!!"
argument_list|)
throw|;
block|}
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|inputs
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|int
index|[]
name|echoIntArray
parameter_list|(
name|int
index|[]
name|ar
parameter_list|,
name|Exchange
name|ex
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No CONTEXT!!!"
argument_list|)
throw|;
block|}
return|return
name|ar
return|;
block|}
specifier|public
name|String
name|listInput
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|inputs
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No CONTEXT!!!"
argument_list|)
throw|;
block|}
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|inputs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|inputs
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|multiListInput
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|inputs1
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|inputs2
parameter_list|,
name|String
name|x
parameter_list|,
name|int
name|y
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No CONTEXT!!!"
argument_list|)
throw|;
block|}
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|inputs1
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|s
range|:
name|inputs2
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|x
operator|==
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<null>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|y
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|multiInOut
parameter_list|(
name|Holder
argument_list|<
name|String
argument_list|>
name|a
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|b
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|c
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|d
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|e
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|f
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|g
parameter_list|)
block|{
name|String
name|ret
init|=
name|b
operator|.
name|value
operator|+
name|d
operator|.
name|value
operator|+
name|e
operator|.
name|value
decl_stmt|;
name|a
operator|.
name|value
operator|=
literal|"a"
expr_stmt|;
name|b
operator|.
name|value
operator|=
literal|"b"
expr_stmt|;
name|c
operator|.
name|value
operator|=
literal|"c"
expr_stmt|;
name|d
operator|.
name|value
operator|=
literal|"d"
expr_stmt|;
name|e
operator|.
name|value
operator|=
literal|"e"
expr_stmt|;
name|f
operator|.
name|value
operator|=
literal|"f"
expr_stmt|;
name|g
operator|.
name|value
operator|=
literal|"g"
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|List
argument_list|<
name|Foo
argument_list|>
name|listObjectOutput
parameter_list|()
block|{
name|Foo
name|a
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|Foo
name|b
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Foo
index|[]
argument_list|>
name|listObjectArrayOutput
parameter_list|()
block|{
name|Foo
name|a
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|a
operator|.
name|setName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|Foo
name|b
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|Foo
name|c
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|c
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|Foo
name|d
init|=
operator|new
name|Foo
argument_list|()
decl_stmt|;
name|d
operator|.
name|setName
argument_list|(
literal|"d"
argument_list|)
expr_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Foo
index|[]
block|{
name|a
block|,
name|b
block|}
argument_list|,
operator|new
name|Foo
index|[]
block|{
name|c
block|,
name|d
block|}
argument_list|)
return|;
block|}
specifier|public
name|int
name|throwException
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|ServiceTestFault
throws|,
name|CustomException
throws|,
name|ComplexException
block|{
switch|switch
condition|(
name|i
condition|)
block|{
case|case
operator|-
literal|1
case|:
throw|throw
operator|new
name|ServiceTestFault
argument_list|(
literal|"Hello!"
argument_list|)
throw|;
case|case
operator|-
literal|2
case|:
block|{
name|CustomException
name|cex
init|=
operator|new
name|CustomException
argument_list|(
literal|"CE: "
operator|+
name|i
argument_list|)
decl_stmt|;
name|cex
operator|.
name|setA
argument_list|(
literal|"A Value"
argument_list|)
expr_stmt|;
name|cex
operator|.
name|setB
argument_list|(
literal|"B Value"
argument_list|)
expr_stmt|;
throw|throw
name|cex
throw|;
block|}
case|case
operator|-
literal|3
case|:
block|{
name|ComplexException
name|ex
init|=
operator|new
name|ComplexException
argument_list|(
literal|"Throw user fault -3"
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setReason
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|ComplexException
operator|.
name|MyBean
name|bean
init|=
operator|new
name|ComplexException
operator|.
name|MyBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Marco"
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setBeans
argument_list|(
operator|new
name|ComplexException
operator|.
name|MyBean
index|[]
block|{
name|bean
block|}
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
default|default:
throw|throw
operator|new
name|ServiceTestFault
argument_list|(
operator|new
name|ServiceTestFault
operator|.
name|ServiceTestDetails
argument_list|(
name|i
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
return|return
name|msg
return|;
block|}
specifier|public
name|int
name|echoIntDifferentWrapperName
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|i
return|;
block|}
specifier|public
name|Bar
name|createBar
parameter_list|(
name|String
name|val
parameter_list|)
block|{
return|return
operator|new
name|BarImpl
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|listObjectIn
parameter_list|(
name|Holder
argument_list|<
name|List
argument_list|<
name|Foo
index|[]
argument_list|>
argument_list|>
name|foos
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit


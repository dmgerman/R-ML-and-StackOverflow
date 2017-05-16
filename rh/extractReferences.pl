#!/usr/bin/perl

#$/ = "\nFrom ";

use strict;
my $inH = 1;
my $from;
my $mid;
my $prevEmpty;

my @fields;

while (<>) {

 
    chomp;
    if (/^$/) {
        if (defined ($mid)) {
            my $sub = sub_mid($mid);
            print("$sub;$mid;from;$from;\n");
            foreach $a (@fields) {
                print("$sub;$mid;$a\n");
            }
            @fields = ();
            undef($mid);
            undef($from);
        }
        $inH = 0;
    } elsif (/^From / and $inH == 0 and $prevEmpty ) {

        if (defined ($mid)) {
        }
        $inH = 1;

    } elsif (/^From: (.+)$/ and $inH == 1) {
        my $sub = sub_mid($mid);
        print("$sub;$mid;from;$from;\n") if defined($from);
        $from = semi($1);
    } elsif (/^Message-ID: <([^>]+)>/ and $inH == 1) {
        die "[$mid][$1]" if defined ($mid);
        $mid = $1;
    } elsif (/^In-Reply-To: <([^>]+)>/ and $inH == 1) {
        my $f = $1;
        my $f2 = sub_mid($f);
        $f =~ s/(;.*)$//;
        push(@fields, "in-reply-to;$f;$f2");
    } elsif (/^References: <([^>]+)>/ and $inH == 1) {
        my $f = $1;
        my $f2 = sub_mid($f);
        push(@fields, "references;$f;$f2");
    } elsif (/^Followup-to: <([^>]+)>$/ and $inH == 1) {
        my $f = $1;
        my $f2 = sub_mid($f);
        push(@fields, "follow-up;$f;$f2");
    } else {
        ;
    }
    $prevEmpty =  ($_ eq "");
}

sub semi {
    my ($a) = @_;

    $a =~ s/;/<SEMICOLON>/;
    return $a;
}

sub sub_mid {
    my ($a) = @_;

    $a =~ s/@.*$//;
    return $a;
}
